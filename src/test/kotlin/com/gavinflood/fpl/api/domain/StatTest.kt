package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatTest {

    @Test
    fun `stat is successfully serialized`() {
        val stat = Stat(StatType.ASSIST, createPlayer(1, "Joe", "Bloggs"), 3)
        assertTrue(Json.encodeToString(stat).contains(StatType.ASSIST.name))
    }

    @Test
    fun `stat is successfully deserialized`() {
        val stat = Stat(StatType.ASSIST, createPlayer(1, "Joe", "Bloggs"), 3)
        val json = Json.encodeToString(stat)
        assertEquals(3, Json.decodeFromString<Stat>(json).value)
    }

    private fun createPlayer(id: Int, firstName: String, lastName: String): Player {
        return Team(1, "Test Team", 1000, 1000, 1000, 1000, 1000, 1000).run {
            Player(id, firstName, lastName, this, Position.MIDFIELDER, 0, false, 6.0, 4.5, 10.2, 90, 500, 800, 400, 100)
        }
    }

}