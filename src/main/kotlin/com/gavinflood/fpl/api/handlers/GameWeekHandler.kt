package com.gavinflood.fpl.api.handlers

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.GameWeek
import java.time.LocalDateTime

/**
 * Exposes functions that can be called to access data related to game weeks.
 */
class GameWeekHandler : Handler() {

    /**
     * Get all game-weeks for the season.
     */
    fun get(): List<GameWeek> {
        val generalInfo = FantasyAPI.getGeneralInfo()

        val findTeamById = { id: Int ->
            generalInfo.teams
                .map { teamDTO -> mapper.mapTeam(teamDTO) }
                .first { team -> team.id == id }
        }

        val findPlayerById = { id: Int ->
            generalInfo.elements
                .map { playerDTO -> mapper.mapPlayer(playerDTO, findTeamById(playerDTO.team)) }
                .firstOrNull { player -> player.id == id }
        }

        return generalInfo.events.map { event -> mapper.mapGameWeek(event, findPlayerById) }
    }

    /**
     * Get a single game-week given its [id].
     */
    fun get(id: Int): GameWeek = get().first { gameWeek -> gameWeek.id == id }

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
        val gameWeeks = get().filter { gameWeek -> gameWeek.deadline.isBefore(LocalDateTime.now()) }
        return gameWeeks.sumOf { gameWeek -> gameWeek.averageScore }.div(gameWeeks.size)
    }

}