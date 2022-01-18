package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManagerPlayerTest {

    @Test
    fun `manager player is successfully serialized`() {
        val player = createPlayer(1, "Joe", "Bloggs", createTeam(4, "Test Team"), Position.FORWARD)
        val managerPlayer = ManagerPlayer(player, 6, isCaptain = true, isViceCaptain = false)
        assertTrue(Json.encodeToString(managerPlayer).contains("Bloggs"))
    }

    @Test
    fun `manager player is successfully deserialized`() {
        val player = createPlayer(1, "Joe", "Bloggs", createTeam(4, "Test Team"), Position.FORWARD)
        val managerPlayer = ManagerPlayer(player, 6, isCaptain = true, isViceCaptain = false)
        val json = Json.encodeToString(managerPlayer)
        Assertions.assertFalse(Json.decodeFromString<ManagerPlayer>(json).isViceCaptain)
    }

    private fun createTeam(id: Int, name: String): Team {
        return Team(id, name, 1000, 1000, 1000, 1000, 1000, 1000)
    }

    private fun createPlayer(id: Int, firstName: String, lastName: String, team: Team, position: Position): Player {
        return Player(id, firstName, lastName, team, position, 0, false, 6.0, 4.5, 10.2, 90, 1080, 2000, 1000, 100)
    }

}