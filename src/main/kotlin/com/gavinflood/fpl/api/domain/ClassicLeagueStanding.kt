package com.gavinflood.fpl.api.domain

import kotlinx.serialization.Serializable

/**
 * Represents a single manager's position in a classic league.
 */
@Serializable
class ClassicLeagueStanding(
    val managerId: Long,
    val managerName: String,
    val teamName: String,
    val rank: Int,
    val lastRank: Int,
    val totalPoints: Int,
    val pointsThisGameWeek: Int
)