package com.gavinflood.fpl.api.domain

import kotlinx.serialization.Serializable

@Serializable
class ChipPlay(
    val name: String,
    val numPlayed: Int
)