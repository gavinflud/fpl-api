package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.Player
import com.gavinflood.fpl.api.handlers.expressions.PlayerModelBuilder

/**
 * Exposes functions that can be called to access data related to players.
 */
class ManagerHandler : Handler() {

    /**
     * Get a managers players for the current game week.
     */
    fun getPlayersForCurrentGameWeek(managerId: Long): List<Player> {
        return mapper.mapManagerTeam(
            FantasyAPI.getManagerPicks(
                managerId,
                getCurrentGameWeekId()
            )
        ) { playerId -> FantasyAPI.players.get(playerId) }.players.map { it.player }
    }

    /**
     * Given [numFreeTransfers], recommend the best transfers to make that could improve the team.
     */
    fun getRecommendedTransfersForNextGameWeek(managerId: Long, numFreeTransfers: Int): Map<Player, Player> {
        val currentPlayers = getPlayersForCurrentGameWeek(managerId)
        val modelBuilder = PlayerModelBuilder()
        modelBuilder.initializePlayerVariablesWithTotalPointsWeight()
        modelBuilder.addMaxBudgetConstraint()
        modelBuilder.addMaxPlayersPerTeamConstraint()
        modelBuilder.addMaxPlayersPerPositionConstraint()
        modelBuilder.addLowValueDefenderAndGoalkeeperConstraint()
        modelBuilder.addMinimumCurrentPlayersConstraint(currentPlayers, 15 - numFreeTransfers)
        modelBuilder.build().maximise()

        val newPlayers = modelBuilder.getSelectedPlayersAfterOptimization(15)
        val replacedPlayers =
            currentPlayers.filter { currentPlayer -> newPlayers.all { newPlayer -> newPlayer.id != currentPlayer.id } }
        val replacementPlayers =
            newPlayers.filter { newPlayer -> currentPlayers.all { currentPlayer -> currentPlayer.id != newPlayer.id } }
        val transferMap = mutableMapOf<Player, Player>()
        replacedPlayers.forEachIndexed { i, player -> transferMap[player] = replacementPlayers[i] }
        return transferMap
    }

    private fun getCurrentGameWeekId() = FantasyAPI.gameWeeks.get().first { it.isCurrent }.id

}