package com.gavinflood.fpl.api.domain

import kotlinx.serialization.Serializable

/**
 * Represents a single stat for a player.
 */
@Serializable
class Stat(
    val type: StatType,
    val player: Player,
    val value: Int
)

/**
 * Identifies a type of stat.
 */
enum class StatType(val code: String) {

    GOAL("goals_scored"),
    ASSIST("assists"),
    OWN_GOAL("own_goals"),
    PENALTY_SAVE("penalties_saved"),
    PENALTY_MISS("penalties_missed"),
    YELLOW_CARD("yellow_cards"),
    RED_CARD("red_cards"),
    SAVE("saves"),
    BONUS("bonus"),
    BPS("bps");

    companion object {
        private val values = values().associateBy(StatType::code)

        fun getEnumByCode(code: String) = values.getValue(code)
    }

}