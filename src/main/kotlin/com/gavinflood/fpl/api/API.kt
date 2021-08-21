package com.gavinflood.fpl.api

import com.gavinflood.fpl.api.domain.GameWeek
import com.gavinflood.fpl.api.domain.Player
import com.gavinflood.fpl.api.domain.Position
import com.gavinflood.fpl.api.domain.Team
import com.gavinflood.fpl.api.http.Client
import com.gavinflood.fpl.api.mapper.Mapper

/**
 * This is the primary entry-point to get Fantasy Football data. It handles the calls to the official Fantasy Football
 * API and parses the responses, returning the necessary data in more refined objects.
 */
class API {

    private val client = Client()
    private val mapper = Mapper()

    /**
     * Get all game-weeks for the season.
     */
    fun getGameWeeks(): List<GameWeek> = client.getGeneralInfo().events.map { mapper.mapGameWeek(it) }

    /**
     * Get a single game-week given its [id].
     */
    fun getGameWeek(id: Int): GameWeek = mapper.mapGameWeek(client.getGeneralInfo().events.first { it.id == id })

    /**
     * Get all teams in the league.
     */
    fun getTeams(): List<Team> = client.getGeneralInfo().teams.map { mapper.mapTeam(it) }

    /**
     * Get a single team given its [id].
     */
    fun getTeam(id: Int): Team = mapper.mapTeam(client.getGeneralInfo().teams.first { it.id == id })

    /**
     * Get all players in the league.
     */
    fun getPlayers(): List<Player> {
        val generalInfo = client.getGeneralInfo()
        val teams = generalInfo.teams.map { teamDTO -> mapper.mapTeam(teamDTO) }
        val findTeamById = { id: Int -> teams.first { team -> team.id == id } }
        return generalInfo.elements.map { playerDTO -> mapper.mapPlayer(playerDTO, findTeamById) }
    }

    /**
     * Get all players in a given [position].
     */
    fun getPlayersByPosition(position: Position): List<Player> =
        getPlayers().filter { player -> player.position == position }

    /**
     * Get all players in a team given its [id].
     */
    fun getPlayersForTeam(id: Int): List<Player> {
        val generalInfo = client.getGeneralInfo()
        val team = mapper.mapTeam(generalInfo.teams.first { it.id == id })
        return generalInfo.elements
            .filter { playerDTO -> playerDTO.team == id }
            .map { mapper.mapPlayer(it, team) }
    }

    /**
     * Get a single player given their [id].
     */
    fun getPlayer(id: Int): Player = getPlayers().first { player -> player.id == id }

}