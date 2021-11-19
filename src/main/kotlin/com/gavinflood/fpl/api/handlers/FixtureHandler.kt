package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.API
import com.gavinflood.fpl.api.domain.Fixture

/**
 * Exposes functions that can be called to access data related to fixtures.
 */
class FixtureHandler : Handler() {

    /**
     * Get all fixtures for the season.
     */
    fun get(): List<Fixture> {
        val generalInfo = API.getGeneralInfo()

        val findTeamById = { id: Int ->
            generalInfo.teams
                .map { teamDTO -> mapper.mapTeam(teamDTO) }
                .first { team -> team.id == id }
        }

        val findGameWeekById = { id: Int ->
            generalInfo.events
                .map { gameWeekDTO -> mapper.mapGameWeek(gameWeekDTO) }
                .first { gameWeek -> gameWeek.id == id }
        }

        val findPlayerById = { id: Int ->
            generalInfo.elements
                .map { playerDTO -> mapper.mapPlayer(playerDTO, findTeamById(playerDTO.team)) }
                .first { player -> player.id == id }
        }

        return API.getFixtures().map { fixtureDTO ->
            mapper.mapFixture(fixtureDTO, findGameWeekById, findTeamById, findPlayerById)
        }
    }

    /**
     * Get a single fixture given its [id].
     */
    fun get(id: Int): Fixture = get().first { fixture -> fixture.id == id }

    /**
     * Get all fixtures for a team given its [teamId].
     */
    fun getByTeam(teamId: Int): List<Fixture> =
        get().filter { fixture -> fixture.homeTeam.id == teamId || fixture.awayTeam.id == teamId }

}