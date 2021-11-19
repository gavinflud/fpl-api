package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.API
import com.gavinflood.fpl.api.domain.Player
import com.gavinflood.fpl.api.domain.Position

/**
 * Exposes functions that can be called to access data related to players.
 */
class PlayerHandler : Handler() {

    /**
     * Get all players in the league.
     */
    fun get(): List<Player> {
        val generalInfo = API.getGeneralInfo()
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
        val generalInfo = API.getGeneralInfo()
        val team = mapper.mapTeam(generalInfo.teams.first { it.id == id })
        return generalInfo.elements
            .filter { playerDTO -> playerDTO.team == id }
            .map { playerDTO -> mapper.mapPlayer(playerDTO, team) }
    }

}