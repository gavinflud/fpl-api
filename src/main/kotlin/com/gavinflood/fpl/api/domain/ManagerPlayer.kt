package com.gavinflood.fpl.api.domain

import kotlinx.serialization.Serializable

@Serializable
class ManagerPlayer(
    val player: Player,
    val position: Int,
    val isCaptain: Boolean,
    val isViceCaptain: Boolean
)