package com.gavinflood.fpl.api.handler

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.handlers.LeagueHandler
import com.gavinflood.fpl.api.http.Client
import com.gavinflood.fpl.api.http.response.ClassicLeagueResponse
import com.gavinflood.fpl.api.http.response.GeneralResponse
import com.gavinflood.fpl.api.http.response.ManagerHistoryResponse
import com.gavinflood.fpl.api.http.response.ManagerResponse
import com.gavinflood.fpl.api.util.TestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.kotlin.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LeagueHandlerTest : HandlerTest() {

    private val leagueId = 224193L
    private val smallerLeagueId = 1234567L

    @BeforeAll
    fun init() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.classicLeagueResponse)
            .whenever(mockClient)
            .sendRequest(properties.getClassicLeagueStandingsUrl(leagueId), ClassicLeagueResponse::class)

        doReturn(TestUtils.classicLeague2Response)
            .whenever(mockClient)
            .sendRequest(
                properties.getClassicLeagueStandingsUrl(leagueId, standingsPage = 2),
                ClassicLeagueResponse::class
            )

        doReturn(TestUtils.classicLeague4TeamsResponse)
            .whenever(mockClient)
            .sendRequest(properties.getClassicLeagueStandingsUrl(smallerLeagueId), ClassicLeagueResponse::class)

        doReturn(TestUtils.generalResponse)
            .whenever(mockClient)
            .sendRequest(properties.getGeneralUrl(), GeneralResponse::class)

        doReturn(TestUtils.managerResponse)
            .whenever(mockClient)
            .sendRequest(any(), eq(ManagerResponse::class))

        doReturn(TestUtils.manager2Response)
            .whenever(mockClient)
            .sendRequest(properties.getManagerUrl(12345), ManagerResponse::class)

        doReturn(TestUtils.manager3Response)
            .whenever(mockClient)
            .sendRequest(properties.getManagerUrl(23456), ManagerResponse::class)

        doReturn(TestUtils.manager4Response)
            .whenever(mockClient)
            .sendRequest(properties.getManagerUrl(34567), ManagerResponse::class)

        doReturn(TestUtils.managerHistoryResponse)
            .whenever(mockClient)
            .sendRequest(any(), eq(ManagerHistoryResponse::class))

        doReturn(TestUtils.managerHistory2Response)
            .whenever(mockClient)
            .sendRequest(properties.getManagerHistoryUrl(12345), ManagerHistoryResponse::class)

        doReturn(TestUtils.managerHistory3Response)
            .whenever(mockClient)
            .sendRequest(properties.getManagerHistoryUrl(23456), ManagerHistoryResponse::class)

        doReturn(TestUtils.managerHistory4Response)
            .whenever(mockClient)
            .sendRequest(properties.getManagerHistoryUrl(34567), ManagerHistoryResponse::class)

        FantasyAPI.httpClient = mockClient
    }

    @Test
    fun `all standings are returned when iterating over results from get`() {
        var count = 0
        LeagueHandler().get(leagueId).standingsIterator().forEach { _ -> count++ }
        assertEquals(53, count)
    }

    @Test
    fun `most improved manager since last game week is correct`() {
        assertEquals(12345, LeagueHandler().getMostImprovedManagersSinceLastGameWeek(smallerLeagueId).keys.first().id)
    }

    @Test
    fun `most improved manager since last game week returns expected number of results`() {
        assertEquals(3, LeagueHandler().getMostImprovedManagersSinceLastGameWeek(smallerLeagueId, 3).size)
    }

    @Test
    fun `least improved manager since last game week is correct`() {
        assertEquals(23456, LeagueHandler().getLeastImprovedManagersSinceLastGameWeek(smallerLeagueId).keys.first().id)
    }

    @Test
    fun `least improved manager since last game week returns expected number of results`() {
        assertEquals(2, LeagueHandler().getLeastImprovedManagersSinceLastGameWeek(smallerLeagueId, 2).size)
    }

}