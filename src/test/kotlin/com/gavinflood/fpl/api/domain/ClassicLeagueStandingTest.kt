package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClassicLeagueStandingTest {

    @Test
    fun `chip play total is successfully serialized`() {
        val standing = ClassicLeagueStanding(123L, "Joe Bloggs", "Joe's Team", 1, 2, 1000, 100)
        Assertions.assertTrue(Json.encodeToString(standing).contains(standing.managerName))
    }

    @Test
    fun `chip play total is successfully deserialized`() {
        val standing = ClassicLeagueStanding(123L, "Joe Bloggs", "Joe's Team", 1, 2, 1000, 100)
        val json = Json.encodeToString(standing)
        Assertions.assertEquals(standing.totalPoints, Json.decodeFromString<ClassicLeagueStanding>(json).totalPoints)
    }

}