package com.gavinflood.fpl.api

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    print(Json.encodeToString(API.fixtures.get()))
}