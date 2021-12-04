package com.gavinflood.fpl.api.domain

import com.gavinflood.fpl.api.serialize.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * Represents a game-week.
 */
@Serializable
class GameWeek(
    val id: Int,
    val name: String,
    val averageScore: Int,
    val highestScore: Int,
    val isCurrent: Boolean,
    val isNext: Boolean,
    val chipPlays: List<ChipPlayTotal>,
    val numTransfersMade: Int,

    @Serializable(with = LocalDateTimeSerializer::class)
    val deadline: LocalDateTime,

    val mostCaptainedPlayer: Player?,
    val mostViceCaptainedPlayer: Player?
)