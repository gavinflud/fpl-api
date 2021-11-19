package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.API
import com.gavinflood.fpl.api.domain.Team

/**
 * Exposes functions that can be called to access data related to teams.
 */
class TeamHandler : Handler() {

    /**
     * Get all teams in the league.
     */
    fun get(): List<Team> = API.getGeneralInfo().teams.map { mapper.mapTeam(it) }

    /**
     * Get a single team given its [id].
     */
    fun get(id: Int): Team = mapper.mapTeam(API.getGeneralInfo().teams.first { it.id == id })

}