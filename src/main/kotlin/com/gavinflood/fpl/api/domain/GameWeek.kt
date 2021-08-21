package com.gavinflood.fpl.api.domain

import com.gavinflood.fpl.api.serialize.DateSerializer
import kotlinx.serialization.Serializable
import java.util.*

/**
 * Represents a game-week.
 */
@Serializable
class GameWeek(
    val id: Int,
    val name: String,
    val averageScore: Int,
    val isCurrent: Boolean,
    val chipPlays: List<ChipPlay>,
    val numTransfersMade: Int,

    @Serializable(with = DateSerializer::class)
    val deadline: Date,
)