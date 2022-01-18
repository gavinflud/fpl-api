package com.gavinflood.fpl.api.domain

import com.gavinflood.fpl.api.serialize.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * Represents a single fixture between two teams.
 */
@Serializable
class Fixture(
    val id: Int,
    val gameWeek: GameWeek?,

    @Serializable(with = LocalDateTimeSerializer::class)
    val kickoff: LocalDateTime?,

    val homeTeam: Team,
    val awayTeam: Team,
    val homeTeamDifficulty: Int,
    val awayTeamDifficulty: Int,
    val stats: List<Stat>,
) {

    /**
     * Returns true if the kickoff time for this fixture was over 3 hours ago.
     */
    fun isFinished(): Boolean = kickoff!!.isBefore(LocalDateTime.now().minusHours(3))

    /**
     * Returns true if the [team] is involved in this fixture.
     */
    fun isTeamInvolved(team: Team) = homeTeam.id == team.id || awayTeam.id == team.id

}