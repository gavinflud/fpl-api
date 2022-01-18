package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerTest {

    @Test
    fun `player is successfully serialized`() {
        val player = createPlayer(1, "Joe", "Bloggs")
        Assertions.assertTrue(Json.encodeToString(player).contains(player.lastName))
    }

    @Test
    fun `player is successfully deserialized`() {
        val player = createPlayer(156, "Joe", "Bloggs")
        val json = Json.encodeToString(player)
        Assertions.assertEquals(156, Json.decodeFromString<Player>(json).id)
    }

    private fun createPlayer(id: Int, firstName: String, lastName: String): Player {
        return Team(1, "Test Team", 1000, 1000, 1000, 1000, 1000, 1000).run {
            Player(id, firstName, lastName, this, Position.MIDFIELDER, 0, false, 6.0, 4.5, 10.2, 90, 500, 800, 400, 100)
        }
    }

}