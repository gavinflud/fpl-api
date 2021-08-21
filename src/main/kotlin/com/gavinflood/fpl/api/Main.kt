package com.gavinflood.fpl.api

import com.gavinflood.fpl.api.domain.Position
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    val api = API()
    print(Json.encodeToString(api.getPlayersByPosition(Position.FORWARD)))
}