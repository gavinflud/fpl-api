package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.Manager
import com.gavinflood.fpl.api.domain.Player
import com.gavinflood.fpl.api.handlers.expressions.PlayerModelBuilder

/**
 * Exposes functions that can be called to access data related to players.
 */
class ManagerHandler : Handler() {

    /**
     * Get a single manager given their [managerId].
     */
    fun get(managerId: Long): Manager {
        val generalInfo = FantasyAPI.httpClient.getGeneralInfo()
        val historyEntries =
            mapper.mapManagerHistory(FantasyAPI.httpClient.getManagerHistory(managerId), findGameWeekById(generalInfo))
        return mapper.mapManager(FantasyAPI.httpClient.getManager(managerId), historyEntries)
    }

    /**
     * Get a managers players for the current game week.
     */
    fun getPlayersForCurrentGameWeek(managerId: Long): List<Player> {
        return mapper.mapManagerTeam(
            FantasyAPI.httpClient.getManagerPicks(
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
        return getRecommendedTransfersForNextGameWeek(currentPlayers, numFreeTransfers)
    }

    /**
     * Given a list of [currentPlayers], recommend the best transfers to make that could improve the team.
     */
    fun getRecommendedTransfersForNextGameWeek(
        currentPlayers: List<Player>,
        numFreeTransfers: Int
    ): Map<Player, Player> {
        if (numFreeTransfers <= 0) {
            return emptyMap()
        }

        val modelBuilder = PlayerModelBuilder()
        modelBuilder.initializeAllPlayerVariables()
        modelBuilder.addMaxBudgetConstraint()
        modelBuilder.addMaxPlayersPerTeamConstraint()
        modelBuilder.addMaxPlayersPerPositionConstraint()
        modelBuilder.addLowValueDefenderAndGoalkeeperConstraint()
        modelBuilder.addMinimumCurrentPlayersConstraint(currentPlayers, currentPlayers.size - numFreeTransfers)
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

    /**
     * Determine the recommended starting XI for the next game week based on the players in the manager's (identified by
     * [managerId]) squad.
     */
    fun getRecommendedStartingTeamForNextGameWeek(managerId: Long): List<Player> {
        val currentPlayers = getPlayersForCurrentGameWeek(managerId)
        val modelBuilder = PlayerModelBuilder()
        modelBuilder.initializeSpecifiedPlayerVariables(currentPlayers)
        modelBuilder.addMinAndMaxPlayersPerPositionConstraint()
        modelBuilder.build().maximise()
        return modelBuilder.getSelectedPlayersAfterOptimization(11)
    }

    /**
     * Get the ID of the current game week.
     */
    private fun getCurrentGameWeekId() = FantasyAPI.gameWeeks.getCurrentGameWeek().id

}