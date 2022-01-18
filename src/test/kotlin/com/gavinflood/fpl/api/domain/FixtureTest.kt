package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FixtureTest {

    @Test
    fun `fixture in the past is finished`() {
        val team = createTeam(1, "Team 1")
        val fixture = Fixture(1, null, LocalDateTime.now().minusDays(1), team, team, 1, 5, emptyList())
        assertTrue(fixture.isFinished())
    }

    @Test
    fun `fixture in the future is not finished`() {
        val team = createTeam(1, "Team 1")
        val fixture = Fixture(1, null, LocalDateTime.now().plusDays(1), team, team, 1, 5, emptyList())
        assertFalse(fixture.isFinished())
    }

    @Test
    fun `returns true when team is involved in a fixture`() {
        val team1 = createTeam(1, "Team 1")
        val team2 = createTeam(2, "Team 2")
        val fixture = Fixture(1, null, LocalDateTime.now().plusDays(1), team1, team2, 1, 5, emptyList())
        assertTrue(fixture.isTeamInvolved(team2))
    }

    @Test
    fun `returns false when team is not involved in a fixture`() {
        val team1 = createTeam(1, "Team 1")
        val team2 = createTeam(2, "Team 2")
        val fixture = Fixture(1, null, LocalDateTime.now().plusDays(1), team1, team1, 1, 5, emptyList())
        assertFalse(fixture.isTeamInvolved(team2))
    }

    @Test
    fun `fixture is successfully serialized`() {
        val team1 = createTeam(1, "Team 1")
        val team2 = createTeam(2, "Team 2")
        val fixture = Fixture(1, null, LocalDateTime.now(), team1, team2, 1, 5, emptyList())
        assertTrue(Json.encodeToString(fixture).contains(team2.name))
    }

    @Test
    fun `fixture is successfully deserialized`() {
        val team1 = createTeam(1, "Team 1")
        val team2 = createTeam(2, "Team 2")
        val fixture = Fixture(1, null, LocalDateTime.now(), team1, team2, 1, 5, emptyList())
        val json = Json.encodeToString(fixture)
        assertEquals(team1.name, Json.decodeFromString<Fixture>(json).homeTeam.name)
    }

    private fun createTeam(id: Int, name: String): Team {
        return Team(id, name, 1000, 1000, 1000, 1000, 1000, 1000)
    }

}