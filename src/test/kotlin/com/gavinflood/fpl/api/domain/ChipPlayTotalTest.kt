package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChipPlayTotalTest {

    @Test
    fun `chip play total is successfully serialized`() {
        val name = "Test Chip Play"
        val chipPlayTotal = ChipPlayTotal(name, 345)
        Assertions.assertTrue(Json.encodeToString(chipPlayTotal).contains(name))
    }

    @Test
    fun `chip play total is successfully deserialized`() {
        val name = "Test Chip Play"
        val chipPlayTotal = ChipPlayTotal(name, 345)
        val json = Json.encodeToString(chipPlayTotal)
        Assertions.assertEquals(name, Json.decodeFromString<ChipPlayTotal>(json).name)
    }

}