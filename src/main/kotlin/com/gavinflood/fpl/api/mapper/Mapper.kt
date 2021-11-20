package com.gavinflood.fpl.api.mapper

import com.gavinflood.fpl.api.domain.*
import com.gavinflood.fpl.api.http.response.*

/**
 * Maps data in DTOs returned from the official API to custom, more refined domain objects.
 */
class Mapper {

    /**
     * Maps the data in [eventDTO] to a [GameWeek] object.
     */
    fun mapGameWeek(eventDTO: GeneralResponseEvent, getPlayer: (id: Int) -> Player?) = GameWeek(
        eventDTO.id,
        eventDTO.name,
        eventDTO.average_entry_score,
        eventDTO.highest_score,
        eventDTO.is_current,
        eventDTO.chip_plays.map { mapChipPlayTotal(it) },
        eventDTO.transfers_made,
        eventDTO.deadline_time,
        getPlayer(eventDTO.most_captained),
        getPlayer(eventDTO.most_vice_captained)
    )

    /**
     * Maps the data in [chipPlayDTO] to a [ChipPlayTotal] object.
     */
    fun mapChipPlayTotal(chipPlayDTO: GeneralResponseEventChipPlay) =
        ChipPlayTotal(chipPlayDTO.chip_name, chipPlayDTO.num_played)

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
        Position.getEnumById(playerDTO.element_type),
        playerDTO.dreamteam_count,
        playerDTO.in_dreamteam,
        playerDTO.now_cost.toDouble().div(10),
        playerDTO.selected_by_percent,
        playerDTO.total_points,
        playerDTO.minutes,
        playerDTO.transfers_in,
        playerDTO.transfers_out
    )

    /**
     * Maps the data in [fixtureDTO] to a [Fixture] object. Requires [getGameWeek], [getTeam], and [getPlayer]
     * lambdas to get the relevant objects by their id.
     */
    fun mapFixture(
        fixtureDTO: FixturesResponseDetail,
        getGameWeek: (id: Int) -> GameWeek,
        getTeam: (id: Int) -> Team,
        getPlayer: (id: Int) -> Player
    ) = mapFixture(
        fixtureDTO,
        getGameWeek(fixtureDTO.event),
        getTeam(fixtureDTO.team_h),
        getTeam(fixtureDTO.team_a),
        getPlayer
    )

    /**
     * Maps the data in [fixtureDTO] to a [Fixture]
     */
    fun mapFixture(
        fixtureDTO: FixturesResponseDetail,
        gameWeek: GameWeek,
        homeTeam: Team,
        awayTeam: Team,
        getPlayer: (id: Int) -> Player
    ) = Fixture(
        fixtureDTO.id,
        gameWeek,
        fixtureDTO.kickoff_time,
        homeTeam,
        awayTeam,
        fixtureDTO.stats.flatMap { mapStats(it, getPlayer) }
    )

    /**
     * Maps the data in [statsDTO] to a list of [Stat] objects.
     */
    fun mapStats(statsDTO: FixturesResponseStats, getPlayer: (id: Int) -> Player) =
        mutableListOf(statsDTO.a, statsDTO.h)
            .flatten()
            .map { mapStat(it, StatType.getEnumByCode(statsDTO.identifier), getPlayer) }

    /**
     * Maps the data in [statDTO] to a [Stat] object. Requires a [getPlayer] lambda that takes an [Int] argument.
     */
    fun mapStat(statDTO: FixturesResponseStat, type: StatType, getPlayer: (id: Int) -> Player) =
        Stat(type, getPlayer(statDTO.element), statDTO.value)
}