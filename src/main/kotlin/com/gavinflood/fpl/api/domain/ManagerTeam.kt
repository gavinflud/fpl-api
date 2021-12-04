package com.gavinflood.fpl.api.domain

import kotlinx.serialization.Serializable

@Serializable
class ManagerTeam(
    val players: List<ManagerPlayer>
)