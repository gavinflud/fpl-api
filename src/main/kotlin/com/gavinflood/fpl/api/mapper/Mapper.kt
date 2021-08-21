package com.gavinflood.fpl.api.mapper

import com.gavinflood.fpl.api.domain.*
import com.gavinflood.fpl.api.http.response.GeneralResponseEvent
import com.gavinflood.fpl.api.http.response.GeneralResponseEventChipPlay
import com.gavinflood.fpl.api.http.response.GeneralResponsePlayer
import com.gavinflood.fpl.api.http.response.GeneralResponseTeam

/**
 * Maps data in DTOs returned from the official API to custom, more refined domain objects.
 */
class Mapper {

    /**
     * Maps the data in [eventDTO] to a [GameWeek] object.
     */
    fun mapGameWeek(eventDTO: GeneralResponseEvent) = GameWeek(
        eventDTO.id,
        eventDTO.name,
        eventDTO.average_entry_score,
        eventDTO.is_current,
        eventDTO.chip_plays.map { mapChipPlay(it) },
        eventDTO.transfers_made,
        eventDTO.deadline_time
    )

    /**
     * Maps the data in [chipPlayDTO] to a [ChipPlay] object.
     */
    fun mapChipPlay(chipPlayDTO: GeneralResponseEventChipPlay) = ChipPlay(chipPlayDTO.chip_name, chipPlayDTO.num_played)

    /**
     * Maps the data in [teamDTO] to a [Team] object.
     */
    fun mapTeam(teamDTO: GeneralResponseTeam) = Team(
        teamDTO.id,
        teamDTO.name,
        teamDTO.strength_overall_home,
        teamDTO.strength_overall_away,
        teamDTO.strength_attack_home,
        teamDTO.strength_attack_away,
        teamDTO.strength_defence_home,
        teamDTO.strength_defence_away
    )

    /**
     * Maps the data in [playerDTO] to a [Player] object. Requires a [getTeam] lambda that takes an [Int] argument and
     * returns a [Team] to be associated with the player.
     */
    fun mapPlayer(playerDTO: GeneralResponsePlayer, getTeam: (id: Int) -> Team) =
        mapPlayer(playerDTO, getTeam(playerDTO.team))

    /**
     * Maps the data in [playerDTO] to a [Player] object and associates the player with [team].
     */
    fun mapPlayer(playerDTO: GeneralResponsePlayer, team: Team) = Player(
        playerDTO.id,
        playerDTO.first_name,
        playerDTO.second_name,
        team,
        Position.valueOf(playerDTO.element_type),
        playerDTO.dreamteam_count,
        playerDTO.in_dreamteam,
        playerDTO.now_cost.toDouble().div(10),
        playerDTO.selected_by_percent,
        playerDTO.transfers_in,
        playerDTO.transfers_out
    )
}