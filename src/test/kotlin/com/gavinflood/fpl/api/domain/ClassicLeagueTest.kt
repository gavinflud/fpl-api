package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClassicLeagueTest {

    @Test
    fun `request is made to retrieve more standings when page is fully iterated over`() {
        val wrapper = ClassicLeagueStandingsWrapper(
            listOf(createClassicLeagueStanding(), createClassicLeagueStanding()),
            true
        )

        val league = ClassicLeague(1, "Test League", LocalDateTime.now(), wrapper) { pageNumber ->
            if (pageNumber == 2) {
                throw MovedToNextStandingsPageException()
            }

            wrapper
        }

        assertThrows<MovedToNextStandingsPageException> { league.standingsIterator().forEach { _ -> } }
    }

    @Test
    fun `classic league is successfully serialized`() {
        val league = createClassicLeagueWithEndlessPages("Test League")
        assertTrue(Json.encodeToString(league).contains(league.name))
    }

    @Test
    fun `classic league is successfully deserialized`() {
        val league = createClassicLeagueWithEndlessPages("Test League")
        val json = Json.encodeToString(league)
        assertEquals(league.id, Json.decodeFromString<ClassicLeague>(json).id)
    }

    @Test
    fun `classic league standings wrapper is successfully serialized`() {
        val wrapper = ClassicLeagueStandingsWrapper(listOf(createClassicLeagueStanding()), false)
        assertTrue(Json.encodeToString(wrapper).contains(wrapper.standingsList[0].teamName))
    }

    @Test
    fun `classic league standings wrapper is successfully deserialized`() {
        val wrapper = ClassicLeagueStandingsWrapper(listOf(createClassicLeagueStanding()), false)
        val json = Json.encodeToString(wrapper)
        assertEquals(
            wrapper.standingsList[0].totalPoints,
            Json.decodeFromString<ClassicLeagueStandingsWrapper>(json).standingsList[0].totalPoints
        )
    }

    private fun createClassicLeagueWithEndlessPages(leagueName: String): ClassicLeague {
        val wrapper =
            ClassicLeagueStandingsWrapper(listOf(createClassicLeagueStanding(), createClassicLeagueStanding()), true)
        return ClassicLeague(1, leagueName, LocalDateTime.now(), wrapper) { wrapper }
    }

    private fun createClassicLeagueStanding(): ClassicLeagueStanding {
        return ClassicLeagueStanding(123L, "Test Name", "Test Team Name", 1, 1, 100, 50)
    }

}

/**
 * Custom exception to throw when moving to the next standings page, purely for the purpose of unit testing.
 */
internal class MovedToNextStandingsPageException : Exception()