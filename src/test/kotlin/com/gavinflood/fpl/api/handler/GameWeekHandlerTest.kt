package com.gavinflood.fpl.api.handler

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.handlers.GameWeekHandler
import com.gavinflood.fpl.api.http.Client
import com.gavinflood.fpl.api.http.response.GeneralResponse
import com.gavinflood.fpl.api.util.TestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GameWeekHandlerTest : HandlerTest() {

    @BeforeAll
    fun init() {
        val mockClient = spy<Client>()

        doReturn(TestUtils.generalResponse)
            .whenever(mockClient)
            .sendRequest(properties.getGeneralUrl(), GeneralResponse::class)

        FantasyAPI.httpClient = mockClient
    }

    @Test
    fun `all game weeks are returned from get`() {
        assertEquals(38, GameWeekHandler().get().size)
    }

    @Test
    fun `correct game week is returned from get by ID`() {
        assertEquals(128, GameWeekHandler().get(16).highestScore)
    }

    @Test
    fun `exception thrown when calling get with an invalid ID`() {
        assertThrows<NoSuchElementException> { GameWeekHandler().get(39) }
    }

    @Test
    fun `current game week is returned correctly`() {
        assertEquals(17, GameWeekHandler().getCurrentGameWeek().id)
    }

    @Test
    fun `next game week is returned correctly`() {
        assertEquals(18, GameWeekHandler().getNextGameWeek().id)
    }

    @Test
    fun `next game weeks are returned correctly`() {
        val nextGameWeeks = GameWeekHandler().getNextGameWeeks(4)
        assertEquals(4, nextGameWeeks.size)
        assertEquals(18, nextGameWeeks.first().id)
    }

    @Test
    fun `latest finished game week is returned correctly`() {
        assertEquals(16, GameWeekHandler().getLatestFinishedGameWeek().id)
    }

    @Test
    fun `game week with highest score to date is returned correctly`() {
        val highestScoringWeek = GameWeekHandler().getWeekWithHighestScoreToDate()
        assertEquals(165, highestScoringWeek.highestScore)
        assertEquals(9, highestScoringWeek.id)
    }

    @Test
    fun `average score to date is correctly calculated`() {
        assertEquals(51, GameWeekHandler().getAverageScoreToDate())
    }

}