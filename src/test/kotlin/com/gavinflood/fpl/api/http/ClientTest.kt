package com.gavinflood.fpl.api.http

import com.gavinflood.fpl.api.http.response.*
import com.gavinflood.fpl.api.properties.FplProperties
import com.gavinflood.fpl.api.util.TestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import java.io.IOException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClientTest {

    private val properties = FplProperties()

    @Test
    fun `general info is returned from network request`() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.generalResponse)
            .whenever(mockClient)
            .sendRequest(properties.getGeneralUrl(), GeneralResponse::class)

        val generalInfo = mockClient.getGeneralInfo()
        assertTrue(generalInfo.events.any { it.id == 17 && it.is_current })
    }

    @Test
    fun `fixtures are returned from network request`() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.fixturesResponse)
            .whenever(mockClient)
            .sendRequest(properties.getFixturesUrl(), FixturesResponse::class)

        val fixtures = mockClient.getFixtures()
        assertTrue(fixtures.single { it.id == 6 }.team_h_score == 5)
    }

    @Test
    fun `player is returned from network request`() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.playerResponse)
            .whenever(mockClient)
            .sendRequest(properties.getPlayersUrl(233), PlayerResponse::class)

        val player = mockClient.getPlayer(233)
        assertEquals(1, player.history.single { it.round == 3 }.goals_scored)
    }

    @Test
    fun `game week stats are returned from network request`() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.gameWeekResponse)
            .whenever(mockClient)
            .sendRequest(properties.getGameWeekUrl(15), GameweekResponse::class)

        val gameWeekStats = mockClient.getGameWeek(15)
        assertEquals(1, gameWeekStats.elements.single { it.id == 233L }.stats.assists)
    }

    @Test
    fun `manager details are returned from network request`() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.managerResponse)
            .whenever(mockClient)
            .sendRequest(properties.getManagerUrl(3573238), ManagerResponse::class)

        val manager = mockClient.getManager(3573238)
        assertEquals(10, manager.leagues.classic.size)
    }

    @Test
    fun `manager history is returned from network request`() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.managerHistoryResponse)
            .whenever(mockClient)
            .sendRequest(properties.getManagerHistoryUrl(3573238), ManagerHistoryResponse::class)

        val history = mockClient.getManagerHistory(3573238)
        assertEquals(16, history.current.single { it.event == 7 }.points_on_bench)
    }

    @Test
    fun `manager picks are returned from network request`() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.managerPicksResponse)
            .whenever(mockClient)
            .sendRequest(properties.getManagerPicksUrl(3573238, 15), ManagerPicksResponse::class)

        val managerPicks = mockClient.getManagerPicks(3573238, 15)
        assertEquals(961, managerPicks.entry_history.total_points)
        assertEquals(15, managerPicks.picks.size)
    }

    @Test
    fun `classic league standings are returned from network request`() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.classicLeagueResponse)
            .whenever(mockClient)
            .sendRequest(properties.getClassicLeagueStandingsUrl(124, 1, 1), ClassicLeagueResponse::class)

        val classicLeague = mockClient.getClassicLeagueStandings(124, 1, 1)
        assertEquals(12, classicLeague.standings.results.single { it.id == 23086947L }.rank)
    }

    @Test
    fun `cache is populated after network request`() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.generalResponse)
            .whenever(mockClient)
            .sendRequest(properties.getGeneralUrl(), GeneralResponse::class)

        assertTrue(mockClient.cache.size == 0)
        mockClient.getGeneralInfo()
        assertTrue(mockClient.cache.size == 1)
        assertEquals(mockClient.cache["GENERAL_INFO"], mockClient.getGeneralInfo())
    }

    @Test
    fun `real network request returns expected response`() {
        assertDoesNotThrow { Client().getGeneralInfo() }
    }

    @Test
    fun `invalid network request throws IOException`() {
        assertThrows<IOException> {
            Client().sendRequest(
                properties.getGeneralUrl() + "invalid",
                GeneralResponse::class
            )
        }
    }

}