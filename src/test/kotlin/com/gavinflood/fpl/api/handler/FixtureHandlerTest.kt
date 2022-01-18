package com.gavinflood.fpl.api.handler

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.handlers.FixtureHandler
import com.gavinflood.fpl.api.http.Client
import com.gavinflood.fpl.api.http.response.FixturesResponse
import com.gavinflood.fpl.api.http.response.GeneralResponse
import com.gavinflood.fpl.api.util.TestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FixtureHandlerTest : HandlerTest() {

    @BeforeAll
    fun init() {
        val mockClient = spy<Client>()

        doReturn(TestUtils.fixturesResponse)
            .whenever(mockClient)
            .sendRequest(properties.getFixturesUrl(), FixturesResponse::class)

        doReturn(TestUtils.generalResponse)
            .whenever(mockClient)
            .sendRequest(properties.getGeneralUrl(), GeneralResponse::class)

        FantasyAPI.httpClient = mockClient
    }

    @Test
    fun `all fixtures are returned from get`() {
        assertEquals(380, FixtureHandler().get().size)
    }

    @Test
    fun `correct fixture is returned from get by fixture ID`() {
        assertEquals("Brighton", FixtureHandler().get(22).homeTeam.name)
    }

    @Test
    fun `exception thrown when calling get with an invalid fixture ID`() {
        assertThrows<NoSuchElementException> { FixtureHandler().get(0) }
    }

    @Test
    fun `all fixtures for a specified team are returned`() {
        assertEquals(38, FixtureHandler().getByTeam(13).size)
    }

    @Test
    fun `team has equal number of home and away fixtures`() {
        val teamId = 12
        assertEquals(19, FixtureHandler().getByTeam(teamId).count { fixture -> fixture.homeTeam.id == 12 })
        assertEquals(19, FixtureHandler().getByTeam(teamId).count { fixture -> fixture.awayTeam.id == 12 })
    }

    @Test
    fun `no fixtures returned when calling get with an invalid team ID`() {
        assertTrue(FixtureHandler().getByTeam(21).isEmpty())
    }

    @Test
    fun `all fixtures for a specified game week are returned`() {
        val fixtures = FixtureHandler().getByGameWeek(17)
        assertEquals(8, fixtures.size)
        assertTrue(fixtures.all { it.gameWeek!!.id == 17 })
    }

    @Test
    fun `no fixtures returned when calling get with an invalid game week ID`() {
        assertTrue(FixtureHandler().getByGameWeek(39).isEmpty())
    }

}