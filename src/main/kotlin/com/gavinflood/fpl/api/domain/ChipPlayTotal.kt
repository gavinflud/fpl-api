package com.gavinflood.fpl.api.domain

import kotlinx.serialization.Serializable

/**
 * The total number of a given chip play used.
 */
@Serializable
class ChipPlayTotal(
    val name: String,
    val numPlayed: Int
)