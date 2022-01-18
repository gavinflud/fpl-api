package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.domain.GameWeek
import com.gavinflood.fpl.api.domain.Player
import com.gavinflood.fpl.api.domain.Team
import com.gavinflood.fpl.api.http.response.GeneralResponse
import com.gavinflood.fpl.api.mapper.Mapper

internal fun findTeamById(generalInfo: GeneralResponse): (Int) -> Team {
    return { id: Int ->
        generalInfo.teams
            .first { team -> team.id == id }
            .run { Mapper().mapTeam(this) }
    }
}

internal fun findPlayerById(generalInfo: GeneralResponse): (Int) -> Player? {
    return { id: Int ->
        generalInfo.elements
            .find { player -> player.id == id }
            ?.run { Mapper().mapPlayer(this, findTeamById(generalInfo).invoke(this.team)) }
    }
}

internal fun findGameWeekById(generalInfo: GeneralResponse): (Int) -> GameWeek? {
    return { id: Int ->
        generalInfo.events
            .find { gameWeek -> gameWeek.id == id }
            ?.run { Mapper().mapGameWeek(this, findPlayerById(generalInfo)) }
    }
}