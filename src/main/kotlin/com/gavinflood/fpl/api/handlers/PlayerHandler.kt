package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.Player
import com.gavinflood.fpl.api.domain.Position
import com.gavinflood.fpl.api.domain.StatType

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