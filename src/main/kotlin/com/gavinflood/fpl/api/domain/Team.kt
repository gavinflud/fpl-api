package com.gavinflood.fpl.api.domain

import kotlinx.serialization.Serializable

/**
 * Represents a single team.
 */
@Serializable
class Team(
    val id: Int,
    val name: String,
    val overallHomeStrength: Int,
    val overallAwayStrength: Int,
    val attackHomeStrength: Int,
    val attackAwayStrength: Int,
    val defenceHomeStrength: Int,
    val defenceAwayStrength: Int,
)