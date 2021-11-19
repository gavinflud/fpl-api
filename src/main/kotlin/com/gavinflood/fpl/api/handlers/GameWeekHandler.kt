package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.API
import com.gavinflood.fpl.api.domain.GameWeek

/**
 * Exposes functions that can be called to access data related to game weeks.
 */
class GameWeekHandler : Handler() {

    /**
     * Get all game-weeks for the season.
     */
    fun get(): List<GameWeek> = API.getGeneralInfo().events.map { mapper.mapGameWeek(it) }

    /**
     * Get a single game-week given its [id].
     */
    fun get(id: Int): GameWeek = get().first { it.id == id }

}