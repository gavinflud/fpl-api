package com.gavinflood.fpl.api.handlers.expressions

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.Fixture
import com.gavinflood.fpl.api.domain.Player
import com.gavinflood.fpl.api.domain.Position
import com.gavinflood.fpl.api.domain.Team
import org.ojalgo.optimisation.ExpressionsBasedModel
import org.ojalgo.optimisation.Variable
import java.math.BigDecimal

/**
 * Helps with building an expression-based model where players are the variables.
 */
class PlayerModelBuilder {

    private val maxBudgetConstraintName = "max_budget"
    private val maxPlayersPerTeamConstraintName = "max_for_team_"
    private val maxPlayersPerPositionConstraintName = "max_for_position_"
    private val lowValuePlayersConstraintName = "low_cost_players"
    private val minimumCurrentPlayersConstraintName = "current_players_constraint"

    private val model = ExpressionsBasedModel()
    private val playersByVariableName = FantasyAPI.players.get().associateBy { player -> "player_${player.id}" }

    fun build() = model

    /**
     * Initializes a variable for every player where the weight (objective) is a multiplication of:
     *   - total points for the season (0..n)
     *   - form (0..10)
     *   - difficulty of upcoming 3 fixtures (0..15)
     *
     * This means the aim is to find the players with the highest weight while conforming to the constraints added to
     * the expression.
     */
    fun initializePlayerVariablesWithTotalPointsWeight() {
        val gameWeekIds = FantasyAPI.gameWeeks.getNextGameWeeks(3).map { it.id }
        val upcomingFixtures = FantasyAPI.fixtures.get()
            .filter { fixture -> fixture.gameWeek != null && gameWeekIds.contains(fixture.gameWeek.id) }
        val upcomingFixturesDifficultyByTeam = FantasyAPI.teams.get()
            .associateWith { team -> upcomingFixtures.sumOf { calculateFixtureDifficulty(team, it) } }
            .mapKeys { it.key.id }

        model.addVariables(playersByVariableName.map { (variableName, player) ->
            val upcomingFixturesDifficulty = upcomingFixturesDifficultyByTeam[player.team.id] ?: 5

            // 16 is used here as the max difficulty for three fixtures is 15, so need to avoid multiplying by zero
            val weight = player.totalPoints * player.form * (16 - upcomingFixturesDifficulty)
            Variable.make(variableName)
                .lower(0)
                .upper(1)
                .weight(weight)
                .integer(true)
        })
    }

    /**
     * Limits the total budget to 1000 (£100m).
     */
    fun addMaxBudgetConstraint() {
        val maxBudgetConstraint = model.addExpression(maxBudgetConstraintName)
            .lower(0)
            .upper(1000)
        model.variables.forEach { variable ->
            maxBudgetConstraint.set(
                variable,
                playersByVariableName.getValue(variable.name).currentCost.times(10).toInt()
            )
        }
    }

    /**
     * Allows a maximum of three players from any one team.
     */
    fun addMaxPlayersPerTeamConstraint() {
        FantasyAPI.teams.get().forEach { team ->
            val constraint = model.addExpression("$maxPlayersPerTeamConstraintName${team.id}")
                .lower(0)
                .upper(3)
            model.variables.filter { playersByVariableName.getValue(it.name).team.id == team.id }
                .forEach { constraint.set(it, 1) }
        }
    }

    /**
     * Only a specific number of players are allowed per position. This introduces that constraint.
     */
    fun addMaxPlayersPerPositionConstraint() {
        mapOf(
            Position.GOALKEEPER.name to 2,
            Position.DEFENDER.name to 5,
            Position.MIDFIELDER.name to 5,
            Position.FORWARD.name to 3
        ).forEach { (name, max) ->
            val constraint = model.addExpression("$maxPlayersPerPositionConstraintName${name.lowercase()}")
                .lower(max)
                .upper(max)
            model.variables.filter { playersByVariableName.getValue(it.name).position.name == name }
                .forEach { constraint.set(it, 1) }
        }
    }

    /**
     * To avoid a scenario where a lot of money is spent on a substitute goalkeeper or the third sub outfielder, this
     * adds a constraint allowing a max of £4.5m for each of these positions.
     */
    fun addLowValueDefenderAndGoalkeeperConstraint() {
        val lowCostPlayersConstraint = model.addExpression(lowValuePlayersConstraintName).lower(2)
        model.variables.forEach { variable ->
            val player = playersByVariableName.getValue(variable.name)
            if (listOf(Position.GOALKEEPER.name, Position.DEFENDER.name).contains(player.position.name)
                && player.currentCost.times(10).toInt() < 45
            ) {
                lowCostPlayersConstraint.set(variable, 1)
            }
        }
    }

    /**
     * Given the [currentPlayers] that make up a team, ensure that at least [minimumToKeep] of them remain.
     */
    fun addMinimumCurrentPlayersConstraint(currentPlayers: List<Player>, minimumToKeep: Int) {
        val currentPlayersConstraint =
            model.addExpression(minimumCurrentPlayersConstraintName).lower(minimumToKeep).upper(15)
        model.variables.forEach { variable ->
            val player = playersByVariableName.getValue(variable.name)
            if (currentPlayers.any { it.id == player.id }) {
                currentPlayersConstraint.set(variable, 1)
            }
        }
    }

    /**
     * Selecting the top [numToSelect] players after running maximize() or minimize().
     */
    fun getSelectedPlayersAfterOptimization(numToSelect: Int): List<Player> {
        return model.variables
            .filter { it.value.compareTo(BigDecimal.ZERO) == 1 }
            .toMutableList()
            .apply { this.sortByDescending { it.value } }
            .subList(0, numToSelect)
            .map { playersByVariableName.getValue(it.name) }
    }

    /**
     * Calculate the specified [fixture] difficult for [team].
     */
    private fun calculateFixtureDifficulty(team: Team, fixture: Fixture): Int {
        if (team.id == fixture.homeTeam.id) {
            return fixture.homeTeamDifficulty
        } else if (team.id == fixture.awayTeam.id) {
            return fixture.awayTeamDifficulty
        }

        return 0
    }

}