package com.gavinflood.fpl.api.domain

import kotlinx.serialization.Serializable

/**
 * Represents the stats for a specific manager over a single game week.
 */
@Serializable
class ManagerHistoryEntry(
    val gameWeek: GameWeek,
    val points: Int,
    val totalPoints: Int,
    val rank: Int,
    val moneyInBank: Int,
    val teamValue: Int,
    val transfers: Int,
    val transfersCost: Int,
    val pointsOnBench: Int
)