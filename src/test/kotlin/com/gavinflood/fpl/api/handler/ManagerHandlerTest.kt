package com.gavinflood.fpl.api.handler

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.Player
import com.gavinflood.fpl.api.domain.Position
import com.gavinflood.fpl.api.handlers.ManagerHandler
import com.gavinflood.fpl.api.http.Client
import com.gavinflood.fpl.api.http.response.*
import com.gavinflood.fpl.api.util.TestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManagerHandlerTest : HandlerTest() {

    private val managerId = 3573238L
    private val currentGameWeekId = 17

    private lateinit var recommendedTransferMap: Map<Player, Player>
    private lateinit var recommendedStartingTeam: List<Player>

    @BeforeAll
    fun init() {
        val mockClient = spy<Client>()
        doReturn(TestUtils.managerPicksResponse)
            .whenever(mockClient)
            .sendRequest(properties.getManagerPicksUrl(managerId, currentGameWeekId), ManagerPicksResponse::class)

        doReturn(TestUtils.generalResponse)
            .whenever(mockClient)
            .sendRequest(properties.getGeneralUrl(), GeneralResponse::class)

        doReturn(TestUtils.fixturesResponse)
            .whenever(mockClient)
            .sendRequest(properties.getFixturesUrl(), FixturesResponse::class)

        doReturn(TestUtils.managerResponse)
            .whenever(mockClient)
            .sendRequest(properties.getManagerUrl(managerId), ManagerResponse::class)

        doReturn(TestUtils.managerHistoryResponse)
            .whenever(mockClient)
            .sendRequest(properties.getManagerHistoryUrl(managerId), ManagerHistoryResponse::class)

        FantasyAPI.httpClient = mockClient

        recommendedTransferMap = ManagerHandler().getRecommendedTransfersForNextGameWeek(managerId, 2)
        recommendedStartingTeam = ManagerHandler().getRecommendedStartingTeamForNextGameWeek(managerId)
    }

    @Test
    fun `correct manager is returned from get by ID`() {
        assertEquals("Flood", ManagerHandler().get(managerId).lastName)
    }

    @Test
    fun `number of manager history entries matches number of game weeks started`() {
        assertEquals(17, ManagerHandler().get(managerId).history.size)
    }

    @Test
    fun `15 players returned for current game week`() {
        assertEquals(15, ManagerHandler().getPlayersForCurrentGameWeek(managerId).size)
    }

    @Test
    fun `correct player included in squad for current game week`() {
        assertTrue(ManagerHandler().getPlayersForCurrentGameWeek(managerId).any { it.lastName == "Salah" })
    }

    @Test
    fun `no transfers recommended when passing 0`() {
        assertEquals(0, ManagerHandler().getRecommendedTransfersForNextGameWeek(managerId, 0).size)
    }

    @Test
    fun `correct number of transfers recommended when passing non-zero`() {
        assertEquals(2, recommendedTransferMap.size)
    }

    @Test
    fun `expected transfer is recommended for next game week`() {
        assertTrue(recommendedTransferMap.any { entry ->
            entry.key.lastName == "Son" && entry.value.lastName == "Dennis"
        })
    }

    @Test
    fun `team contains 11 players`() {
        assertEquals(11, recommendedStartingTeam.size)
    }

    @Test
    fun `team contains minimum number of players per position`() {
        assertEquals(1, recommendedStartingTeam.count { it.position == Position.GOALKEEPER })
        assertTrue(recommendedStartingTeam.count { it.position == Position.DEFENDER } in 3..5)
        assertTrue(recommendedStartingTeam.count { it.position == Position.MIDFIELDER } in 2..5)
        assertTrue(recommendedStartingTeam.count { it.position == Position.FORWARD } in 1..3)
    }

}