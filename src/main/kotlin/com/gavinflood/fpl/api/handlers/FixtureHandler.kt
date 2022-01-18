package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.Fixture

/**
 * Exposes functions that can be called to access data related to fixtures.
 */
class FixtureHandler : Handler() {

    /**
     * Get all fixtures for the season.
     */
    fun get(): List<Fixture> {
        val generalInfo = FantasyAPI.httpClient.getGeneralInfo()
        return FantasyAPI.httpClient.getFixtures().map { fixtureDTO ->
            mapper.mapFixture(
                fixtureDTO,
                findGameWeekById(generalInfo),
                findTeamById(generalInfo),
                findPlayerById(generalInfo)
            )
        }
    }

    /**
     * Get a single fixture given its [id].
     */
    fun get(id: Int): Fixture {
        val generalInfo = FantasyAPI.httpClient.getGeneralInfo()
        return FantasyAPI.httpClient.getFixtures().first { it.id == id }.run {
            mapper.mapFixture(
                this,
                findGameWeekById(generalInfo),
                findTeamById(generalInfo),
                findPlayerById(generalInfo)
            )
        }
    }

    /**
     * Get all fixtures for a team given its [teamId].
     */
    fun getByTeam(teamId: Int): List<Fixture> {
        val generalInfo = FantasyAPI.httpClient.getGeneralInfo()
        return FantasyAPI.httpClient.getFixtures()
            .filter { fixture -> fixture.team_h == teamId || fixture.team_a == teamId }
            .map {
                mapper.mapFixture(
                    it,
                    findGameWeekById(generalInfo),
                    findTeamById(generalInfo),
                    findPlayerById(generalInfo)
                )
            }
    }

    /**
     * Get all fixtures for a given [gameWeekId].
     */
    fun getByGameWeek(gameWeekId: Int): List<Fixture> {
        val generalInfo = FantasyAPI.httpClient.getGeneralInfo()
        return FantasyAPI.httpClient.getFixtures()
            .filter { fixture -> fixture.event == gameWeekId }
            .map {
                mapper.mapFixture(
                    it,
                    findGameWeekById(generalInfo),
                    findTeamById(generalInfo),
                    findPlayerById(generalInfo)
                )
            }
    }

}