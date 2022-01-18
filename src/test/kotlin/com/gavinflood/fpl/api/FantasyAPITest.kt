package com.gavinflood.fpl.api

import com.gavinflood.fpl.api.http.Client
import com.gavinflood.fpl.api.http.response.ClassicLeagueResponse
import com.gavinflood.fpl.api.http.response.FixturesResponse
import com.gavinflood.fpl.api.http.response.GeneralResponse
import com.gavinflood.fpl.api.http.response.ManagerPicksResponse
import com.gavinflood.fpl.api.properties.FplProperties
import com.gavinflood.fpl.api.util.TestUtils
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FantasyAPITest {

    private val properties = FplProperties()
    private val managerId = 3573238L
    private val currentGameWeekId = 17
    private val leagueId = 123L

    @BeforeAll
    fun init() {
        val mockClient = spy<Client>()

        doReturn(TestUtils.generalResponse)
            .whenever(mockClient)
            .sendRequest(properties.getGeneralUrl(), GeneralResponse::class)

        doReturn(TestUtils.fixturesResponse)
            .whenever(mockClient)
            .sendRequest(properties.getFixturesUrl(), FixturesResponse::class)

        doReturn(TestUtils.managerPicksResponse)
            .whenever(mockClient)
            .sendRequest(properties.getManagerPicksUrl(managerId, currentGameWeekId), ManagerPicksResponse::class)

        doReturn(TestUtils.classicLeagueResponse)
            .whenever(mockClient)
            .sendRequest(properties.getClassicLeagueStandingsUrl(leagueId), ClassicLeagueResponse::class)

        FantasyAPI.httpClient = mockClient
    }

    @Test
    fun `team handler methods can be called`() {
        assertDoesNotThrow { FantasyAPI.teams.get(10) }
    }

    @Test
    fun `game week handler methods can be called`() {
        assertDoesNotThrow { FantasyAPI.gameWeeks.get() }
    }

    @Test
    fun `player handler methods can be called`() {
        assertDoesNotThrow { FantasyAPI.players.get(212) }
    }

    @Test
    fun `fixture handler methods can be called`() {
        assertDoesNotThrow { FantasyAPI.fixtures.getByTeam(13) }
    }

    @Test
    fun `manager handler methods can be called`() {
        assertDoesNotThrow { FantasyAPI.managers.getPlayersForCurrentGameWeek(managerId) }
    }

    @Test
    fun `league handler methods can be called`() {
        assertDoesNotThrow { FantasyAPI.leagues.get(leagueId) }
    }

    @Test
    fun `test cache object changes when updating flush interval`() {
        val originalCache = FantasyAPI.httpClient.cache
        FantasyAPI.setCacheFlushIntervalInMillis(TimeUnit.SECONDS.toMillis(60))
        assertNotEquals(FantasyAPI.httpClient.cache, originalCache)
    }

}