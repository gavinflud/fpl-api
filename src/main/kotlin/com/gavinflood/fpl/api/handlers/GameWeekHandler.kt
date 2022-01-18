package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.GameWeek

/**
 * Exposes functions that can be called to access data related to game weeks.
 */
class GameWeekHandler : Handler() {

    /**
     * Get all game-weeks for the season.
     */
    fun get(): List<GameWeek> {
        val generalInfo = FantasyAPI.httpClient.getGeneralInfo()
        return generalInfo.events.map { event -> mapper.mapGameWeek(event, findPlayerById(generalInfo)) }
    }

    /**
     * Get a single game-week given its [id].
     */
    fun get(id: Int): GameWeek = get().first { gameWeek -> gameWeek.id == id }

    /**
     * Gets the current game week.
     */
    fun getCurrentGameWeek() = get().first { gameWeek -> gameWeek.isCurrent }

    /**
     * Gets the next game week.
     */
    fun getNextGameWeek() = get().first { gameWeek -> gameWeek.isNext }

    /**
     * Get the next [numberOfGameWeeks] as a list.
     */
    fun getNextGameWeeks(numberOfGameWeeks: Int): List<GameWeek> {
        val currentGameWeek = getCurrentGameWeek()
        return get().filter { gameWeek ->
            gameWeek.id > currentGameWeek.id && gameWeek.id <= currentGameWeek.id.plus(numberOfGameWeeks)
        }
    }

    /**
     * Get the latest finished game week (not always going to be the one before [GameWeek.isCurrent] is true).
     */
    fun getLatestFinishedGameWeek() = get().filter { gameWeek -> gameWeek.isFinished }
        .maxByOrNull { gameWeek -> gameWeek.deadline }!!

    /**
     * Get the game week with the highest score so far.
     */
    fun getWeekWithHighestScoreToDate(): GameWeek {
        val gameWeeks = get().toMutableList()
        gameWeeks.sortByDescending { gameWeek -> gameWeek.highestScore }
        return gameWeeks.first()
    }

    /**
     * Get the average score per week to date.
     */
    fun getAverageScoreToDate(): Int {
        val gameWeeks = get().filter { gameWeek -> gameWeek.isFinished }
        return gameWeeks.sumOf { gameWeek -> gameWeek.averageScore }.div(gameWeeks.size)
    }

}