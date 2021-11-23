package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.Player
import com.gavinflood.fpl.api.domain.Position
import com.gavinflood.fpl.api.domain.StatType
import org.ojalgo.optimisation.ExpressionsBasedModel
import org.ojalgo.optimisation.Variable
import java.math.BigDecimal

/**
 * Exposes functions that can be called to access data related to players.
 */
class PlayerHandler : Handler() {

    /**
     * Get all players in the league.
     */
    fun get(): List<Player> {
        val generalInfo = FantasyAPI.getGeneralInfo()
        val findTeamById = { id: Int ->
            generalInfo.teams
                .map { teamDTO -> mapper.mapTeam(teamDTO) }
                .first { team -> team.id == id }
        }
        return generalInfo.elements.map { playerDTO -> mapper.mapPlayer(playerDTO, findTeamById) }
    }

    /**
     * Get a single player given their [id].
     */
    fun get(id: Int): Player = get().first { player -> player.id == id }

    /**
     * Get all players in a given [position].
     */
    fun getByPosition(position: Position): List<Player> =
        get().filter { player -> player.position == position }

    /**
     * Get all players in a team given its [id].
     */
    fun getByTeam(id: Int): List<Player> {
        val generalInfo = FantasyAPI.getGeneralInfo()
        val team = mapper.mapTeam(generalInfo.teams.first { it.id == id })
        return generalInfo.elements
            .filter { playerDTO -> playerDTO.team == id }
            .map { playerDTO -> mapper.mapPlayer(playerDTO, team) }
    }

    /**
     * Get the top scoring player(s) for a particular [statType].
     */
    fun getTopScoringPlayersByStatType(statType: StatType): Map<Player, Int> =
        getPlayersWithHighestStatValue(getTotalStatValuesPerPlayer(statType))

    /**
     * Get the top scoring player(s) for a particular [statType] and [position].
     */
    fun getTopScoringPlayersByStatTypeAndPosition(statType: StatType, position: Position): Map<Player, Int> =
        getPlayersWithHighestStatValue(getTotalStatValuesPerPlayer(statType)) { player -> player.position == position }

    /**
     * Get the lowest scoring player(s) for a particular [statType].
     */
    fun getLowestScoringPlayersByStatType(statType: StatType): Map<Player, Int> =
        getPlayersWithLowestStatValue(getTotalStatValuesPerPlayer(statType))

    /**
     * Get the lowest scoring player(s) for a particular [statType] and [position].
     */
    fun getLowestScoringPlayersByStatTypeAndPosition(statType: StatType, position: Position): Map<Player, Int> =
        getPlayersWithLowestStatValue(getTotalStatValuesPerPlayer(statType)) { player -> player.position == position }

    /**
     * Get the best value players. Results limited to top [numberToGet].
     */
    fun getBestValuePlayers(numberToGet: Int): List<Player> = getBestValuePlayers(numberToGet) { true }

    /**
     * Get the best value players in a specific [position]. Results limited to top [numberToGet].
     */
    fun getBestValuePlayersByPosition(numberToGet: Int, position: Position) =
        getBestValuePlayers(numberToGet) { player -> player.position == position }

    /**
     * Get the best possible current squad based on total points so far this season.
     */
    fun getBestPossibleTeamBasedOnCurrentSeason(): List<Player> {
        val playersByVariableName = get().associateBy { player -> "player_${player.id}" }

        // Players are the variables, and they are weighted by their total points to date
        val variables = playersByVariableName.map { (variableName, player) ->
            Variable.make(variableName).lower(0).upper(1).weight(player.totalPoints).integer(true)
        }

        val model = ExpressionsBasedModel()
        model.addVariables(variables)

        // Total cost of players must be less than 1000 (£100m with one decimal place)
        val maxBudgetConstraint = model.addExpression("max_budget").lower(0).upper(1000)
        variables.forEach { variable ->
            maxBudgetConstraint.set(
                variable,
                playersByVariableName.getValue(variable.name).currentCost.times(10).toInt()
            )
        }

        // Maximum of three players from any one team
        FantasyAPI.teams.get().forEach { team ->
            val constraint = model.addExpression("max_for_team_${team.id}").lower(0).upper(3)
            variables.filter { playersByVariableName.getValue(it.name).team.id == team.id }
                .forEach { constraint.set(it, 1) }
        }

        // Must have 2 goalkeepers, 5 defenders, 5 midfielders, and 3 forwards
        mapOf(
            Position.GOALKEEPER.name to 2,
            Position.DEFENDER.name to 5,
            Position.MIDFIELDER.name to 5,
            Position.FORWARD.name to 3
        ).forEach { (name, max) ->
            val constraint = model.addExpression("max_for_pos_${name.lowercase()}").lower(max).upper(max)
            variables.filter { playersByVariableName.getValue(it.name).position.name == name }
                .forEach { constraint.set(it, 1) }
        }

        // Aim for two low-cost players to avoid having an expensive sub keeper or third sub
        val lowCostPlayersConstraint = model.addExpression("low_cost_players").lower(2)
        variables.forEach { variable ->
            val player = playersByVariableName.getValue(variable.name)
            if (listOf(Position.GOALKEEPER.name, Position.DEFENDER.name).contains(player.position.name)
                && player.currentCost.times(10).toInt() < 45
            ) {
                lowCostPlayersConstraint.set(variable, 1)
            }
        }

        val result = model.maximise()

        // Selecting the top 15 variables with the highest value as there are instances where more than 15 are selected
        val selectedVariables = model.variables
            .filter { it.value.compareTo(BigDecimal.ZERO) == 1 }
            .toMutableList()
            .apply { this.sortByDescending { it.value } }
            .subList(0, 15)
        return selectedVariables.map { playersByVariableName.getValue(it.name) }
    }

    /**
     * Get the best value players. Results limited to top [numberToGet] and can be filtered by passing a [condition].
     * The best value is defined as having the highest total points when divided by the player's current cost.
     */
    private fun getBestValuePlayers(numberToGet: Int, condition: (player: Player) -> Boolean): List<Player> {
        val players = get().filter(condition).toMutableList()
        players.sortByDescending { player ->
            player.totalPoints.toFloat().div(player.currentCost)
        }
        return players.subList(0, numberToGet)
    }

    /**
     * Sum up the total values for a specific [statType] for each player. Returns a map with the player ID as the key
     * and the total as the value.
     */
    private fun getTotalStatValuesPerPlayer(statType: StatType): Map<Int, Int> {
        val totalsPerPlayer = mutableMapOf<Int, Int>()

        val fixtures = FantasyAPI.fixtures.get()
        fixtures
            .flatMap { fixture -> fixture.stats }
            .forEach { stat ->
                if (stat.type == statType) {
                    val id = stat.player.id

                    if (totalsPerPlayer.containsKey(id)) {
                        totalsPerPlayer[id] = totalsPerPlayer.getValue(id) + stat.value
                    } else {
                        totalsPerPlayer[id] = stat.value
                    }
                }

            }

        return totalsPerPlayer
    }

    /**
     * Find the highest value in [totalsPerPlayer] and returns the players that match that value.
     */
    private fun getPlayersWithHighestStatValue(
        totalsPerPlayer: Map<Int, Int>,
        condition: (player: Player) -> Boolean = { true }
    ): Map<Player, Int> {
        var highestTotal = 0
        val playersWithHighestTotal = mutableMapOf<Player, Int>()
        val playerLookup = get().associateBy { player -> player.id }.filterValues(condition)

        totalsPerPlayer.forEach { (playerId, goals) ->
            val player = playerLookup[playerId]

            if (player != null) {
                if (goals > highestTotal) {
                    playersWithHighestTotal.clear()
                }

                if (goals >= highestTotal) {
                    highestTotal = goals
                    playersWithHighestTotal[playerLookup.getValue(playerId)] = goals
                }
            }
        }

        return playersWithHighestTotal
    }

    /**
     * Find the lowest value in [totalsPerPlayer] and returns the players that match that value.
     */
    private fun getPlayersWithLowestStatValue(
        totalsPerPlayer: Map<Int, Int>,
        condition: (player: Player) -> Boolean = { true }
    ): Map<Player, Int> {
        var lowestTotal = Int.MAX_VALUE
        val playersWithLowestTotal = mutableMapOf<Player, Int>()
        val playerLookup = get().associateBy { player -> player.id }.filterValues(condition)

        totalsPerPlayer.forEach { (playerId, goals) ->
            val player = playerLookup[playerId]

            if (player != null) {
                if (goals < lowestTotal) {
                    playersWithLowestTotal.clear()
                }

                if (goals <= lowestTotal) {
                    lowestTotal = goals
                    playersWithLowestTotal[playerLookup.getValue(playerId)] = goals
                }
            }
        }

        // Some players may not appear in totalsPerPlayer, meaning they have 0 which is the lowest possible value
        val playersNotInTotals = playerLookup.filterValues { player -> !totalsPerPlayer.containsKey(player.id) }

        return if (playersNotInTotals.isNotEmpty())
            playersNotInTotals.values.associateWith { 0 }
        else playersWithLowestTotal
    }


}