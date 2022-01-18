package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeamTest {

    @Test
    fun `team is successfully serialized`() {
        val team = createTeam(12, "Test Team")
        Assertions.assertTrue(Json.encodeToString(team).contains(team.name))
    }

    @Test
    fun `team is successfully deserialized`() {
        val team = createTeam(12, "Test Team")
        val json = Json.encodeToString(team)
        Assertions.assertEquals(12, Json.decodeFromString<Team>(json).id)
    }

    private fun createTeam(id: Int, name: String): Team {
        return Team(id, name, 1000, 1000, 1000, 1000, 1000, 1000)
    }

}