package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.ClassicLeague
import com.gavinflood.fpl.api.domain.ClassicLeagueStandingsWrapper
import com.gavinflood.fpl.api.domain.Manager
import com.gavinflood.fpl.api.http.TooManyRequestsToServerException

/**
 * Exposes functions that can be called to access data related to FPL leagues.
 */
class LeagueHandler : Handler() {

    /**
     * Get a league's info and standings (paginated using [standingsPage]).
     */
    fun get(leagueId: Long, standingsPage: Int = 1): ClassicLeague {
        return mapper.mapClassicLeague(
            FantasyAPI.httpClient.getClassicLeagueStandings(leagueId, 1, standingsPage)
        ) { pageNumber -> handleRetrievingFurtherStandings(leagueId, pageNumber) }
    }

    /**
     * Get the most improved manager(s) in a league when comparing the latest completed game week against the one prior.
     * Returns a map with the manager as they key and the number of points their game-week total increased by as the
     * value.
     */
    fun getMostImprovedManagersSinceLastGameWeek(leagueId: Long, numManagersToShow: Int = 1): Map<Manager, Int> {
        val improvementsByManager = getImprovementsByManagerSinceLastGameWeek(leagueId)
        val cutoffPoint = improvementsByManager.values.sortedDescending()[numManagersToShow - 1]
        return improvementsByManager.filterValues { improvement -> improvement >= cutoffPoint }
    }

    /**
     * Get the least improved manager(s) in a league when comparing the latest completed game week against the one
     * prior. Returns a map with the manager as they key and the number of points their game-week total increased by as
     * the value.
     */
    fun getLeastImprovedManagersSinceLastGameWeek(leagueId: Long, numManagersToShow: Int = 1): Map<Manager, Int> {
        val improvementsByManager = getImprovementsByManagerSinceLastGameWeek(leagueId)
        val cutoffPoint = improvementsByManager.values.sorted()[numManagersToShow - 1]
        return improvementsByManager.filterValues { improvement -> improvement <= cutoffPoint }
    }

    /**
     * Utility function to handle retrieving standings past the initial page. This will wait a second if it hits an
     * exception for making too many requests, after which it will try to request again.
     */
    private fun handleRetrievingFurtherStandings(leagueId: Long, pageNumber: Int): ClassicLeagueStandingsWrapper {
        return try {
            FantasyAPI.httpClient.getClassicLeagueStandings(leagueId, 1, pageNumber).run {
                ClassicLeagueStandingsWrapper(
                    standings.results.map { mapper.mapClassicLeagueStanding(it) },
                    standings.has_next
                )
            }
        } catch (exception: TooManyRequestsToServerException) {
            Thread.sleep(1000)
            handleRetrievingFurtherStandings(leagueId, pageNumber)
        }
    }

    private fun getImprovementsByManagerSinceLastGameWeek(leagueId: Long): Map<Manager, Int> {
        val managerIds = mutableListOf<Long>()
        get(leagueId).standingsIterator().forEach { standing -> managerIds.add(standing.managerId) }
        val currentGameWeek = FantasyAPI.gameWeeks.getLatestFinishedGameWeek()
        val previousGameWeek = FantasyAPI.gameWeeks.get(currentGameWeek.id - 1)

        return managerIds.map { FantasyAPI.managers.get(it) }.associateWith { manager ->
            manager.history.single { it.gameWeek.id == currentGameWeek.id }.points -
                manager.history.single { it.gameWeek.id == previousGameWeek.id }.points
        }
    }

}