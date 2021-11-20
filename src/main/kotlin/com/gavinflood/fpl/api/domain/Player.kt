package com.gavinflood.fpl.api.domain

import kotlinx.serialization.Serializable

/**
 * Represents a single player.
 */
@Serializable
class Player(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val team: Team,
    val position: Position,
    val dreamTeamCount: Int,
    val inDreamTeam: Boolean,
    val currentCost: Double,
    val selectedByPercentage: Double,
    val totalPoints: Int,
    val totalMinutes: Int,
    val transfersIn: Int,
    val transfersOut: Int
)

/**
 * Represents the position a player can play.
 */
enum class Position(val id: Int) {

    GOALKEEPER(1),
    DEFENDER(2),
    MIDFIELDER(3),
    FORWARD(4);

    companion object {
        private val values = values().associateBy(Position::id)

        fun getEnumById(id: Int) = values.getValue(id)
    }

}