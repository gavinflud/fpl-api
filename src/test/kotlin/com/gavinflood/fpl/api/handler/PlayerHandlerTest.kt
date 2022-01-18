package com.gavinflood.fpl.api.handler

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.domain.Player
import com.gavinflood.fpl.api.domain.Position
import com.gavinflood.fpl.api.domain.StatType
import com.gavinflood.fpl.api.handlers.FixtureHandler
import com.gavinflood.fpl.api.handlers.GameWeekHandler
import com.gavinflood.fpl.api.handlers.PlayerHandler
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
class PlayerHandlerTest : HandlerTest() {

    private lateinit var bestPossibleTeam: List<Player>
    private lateinit var bestPossibleTeamForFreeHit: List<Player>
    private lateinit var topFiveRecommendedGoalkeepers: List<Player>
    private lateinit var topFiveRecommendedForwardDifferentials: List<Player>

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

        bestPossibleTeam = PlayerHandler().getBestPossibleTeamBasedOnCurrentSeason()
        bestPossibleTeamForFreeHit = PlayerHandler().getBestPossibleTeamForFreeHit()
        topFiveRecommendedGoalkeepers = PlayerHandler().getRecommendedPlayersByPosition(Position.GOALKEEPER)
        topFiveRecommendedForwardDifferentials = PlayerHandler().getRecommendedDifferentialsByPosition(Position.FORWARD)
    }

    @Test
    fun `get returns players from each club`() {
        val players = PlayerHandler().get()
        for (teamId in 1..20) {
            assertTrue(players.any { player -> player.team.id == teamId })
        }
    }

    @Test
    fun `correct player is returned from get when passing player ID`() {
        assertEquals("Trent", PlayerHandler().get(237).firstName)
    }

    @Test
    fun `exception is thrown when invalid player ID is passed to get`() {
        assertThrows<NoSuchElementException> { PlayerHandler().get(0) }
    }

    @Test
    fun `only players in the specified position are returned from get`() {
        assertTrue(PlayerHandler().getByPosition(Position.DEFENDER).all { it.position == Position.DEFENDER })
    }

    @Test
    fun `only players from the specified team are returned from get`() {
        assertTrue(PlayerHandler().getByTeam(10).all { it.team.id == 10 })
    }

    @Test
    fun `exception is thrown when invalid team ID is passed to get`() {
        assertThrows<NoSuchElementException> { PlayerHandler().getByTeam(22) }
    }

    @Test
    fun `correct player is returned for most goals`() {
        val topScorerMap = PlayerHandler().getTopScoringPlayersByStatType(StatType.GOAL)
        assertEquals("Salah", topScorerMap.keys.single().lastName)
    }

    @Test
    fun `correct player is returned for most assists in a specified position`() {
        val topAssistsMap =
            PlayerHandler().getTopScoringPlayersByStatTypeAndPosition(StatType.ASSIST, Position.DEFENDER)
        assertEquals("Alexander-Arnold", topAssistsMap.keys.single().lastName)
    }

    @Test
    fun `zero is the lowest number of goals scored`() {
        val lowestScorerMap = PlayerHandler().getLowestScoringPlayersByStatType(StatType.GOAL)
        assertTrue(lowestScorerMap.values.all { total -> total == 0 })
    }

    @Test
    fun `goalkeepers with zero saves are returned for fewest saves in a specified position`() {
        val lowestSavesMap =
            PlayerHandler().getLowestScoringPlayersByStatTypeAndPosition(StatType.SAVE, Position.GOALKEEPER)
        assertTrue(lowestSavesMap.keys.all { player -> player.position == Position.GOALKEEPER })
        assertTrue(lowestSavesMap.values.all { total -> total == 0 })
    }

    @Test
    fun `best value player is correct`() {
        assertEquals("Dennis", PlayerHandler().getBestValuePlayers(1).single().lastName)
    }

    @Test
    fun `correct number of players returned from best value players`() {
        assertEquals(5, PlayerHandler().getBestValuePlayers(5).size)
    }

    @Test
    fun `passing invalid number to best value players returns empty list`() {
        assertTrue(PlayerHandler().getBestValuePlayers(-1).isEmpty())
    }

    @Test
    fun `best value player by position is correct`() {
        assertEquals(
            "Gallagher",
            PlayerHandler().getBestValuePlayersByPosition(1, Position.MIDFIELDER).single().lastName
        )
    }

    @Test
    fun `correct number of players returned from best value players by position`() {
        assertEquals(4, PlayerHandler().getBestValuePlayersByPosition(4, Position.GOALKEEPER).size)
    }

    @Test
    fun `passing invalid number to best value players by position returns empty list`() {
        assertTrue(PlayerHandler().getBestValuePlayersByPosition(-5, Position.FORWARD).isEmpty())
    }

    @Test
    fun `best possible team contains 15 players`() {
        assertEquals(15, bestPossibleTeam.size)
    }

    @Test
    fun `best possible team is on or under budget`() {
        assertTrue(bestPossibleTeam.sumOf { it.currentCost } <= 1000)
    }

    @Test
    fun `best possible team has the correct number of players per position`() {
        assertTrue(mapOf(
            Position.GOALKEEPER to 2,
            Position.DEFENDER to 5,
            Position.MIDFIELDER to 5,
            Position.FORWARD to 3
        ).all { (position, total) -> bestPossibleTeam.count { it.position == position } == total })
    }

    @Test
    fun `best possible team has fewer than four players from each club`() {
        assertTrue(bestPossibleTeam.groupBy { player -> player.team.id }.all { (_, players) -> players.size < 4 })
    }

    @Test
    fun `best possible team contains expected players`() {
        assertTrue(bestPossibleTeam.any { player -> player.lastName == "Salah" })
    }

    @Test
    fun `best possible team for free hit contains 15 players`() {
        assertEquals(15, bestPossibleTeamForFreeHit.size)
    }

    @Test
    fun `best possible team for free hit is on or under budget`() {
        assertTrue(bestPossibleTeamForFreeHit.sumOf { it.currentCost } <= 1000)
    }

    @Test
    fun `best possible team for free hit has the correct number of players per position`() {
        assertTrue(mapOf(
            Position.GOALKEEPER to 2,
            Position.DEFENDER to 5,
            Position.MIDFIELDER to 5,
            Position.FORWARD to 3
        ).all { (position, total) -> bestPossibleTeamForFreeHit.count { it.position == position } == total })
    }

    @Test
    fun `best possible team for free hit has fewer than four players from each club`() {
        assertTrue(bestPossibleTeamForFreeHit.groupBy { player -> player.team.id }
            .all { (_, players) -> players.size < 4 })
    }

    @Test
    fun `best possible team for free hit contains expected players`() {
        assertTrue(bestPossibleTeamForFreeHit.any { player -> player.lastName == "Salah" })
    }

    @Test
    fun `best possible team for free hit contains no players without a fixture`() {
        val fixtures = FixtureHandler().getByGameWeek(GameWeekHandler().getNextGameWeek().id)
        assertTrue(bestPossibleTeamForFreeHit.all { player -> fixtures.any { fixture -> fixture.isTeamInvolved(player.team) } })
    }

    @Test
    fun `only players in the specified position are returned as recommended players`() {
        assertTrue(topFiveRecommendedGoalkeepers.all { player -> player.position == Position.GOALKEEPER })
    }

    @Test
    fun `correct number of players are returned as recommended players in a given position`() {
        assertEquals(5, topFiveRecommendedGoalkeepers.size)
    }

    @Test
    fun `top recommended player in position is correct`() {
        assertEquals("de Gea", topFiveRecommendedGoalkeepers.first().lastName)
    }

    @Test
    fun `only players in the specified position are returned as recommended differentials`() {
        assertTrue(topFiveRecommendedForwardDifferentials.all { player -> player.position == Position.FORWARD })
    }

    @Test
    fun `correct number of players are returned as recommended differentials in a given position`() {
        assertEquals(5, topFiveRecommendedForwardDifferentials.size)
    }

    @Test
    fun `recommended differentials have lower than or equal to 5 percent ownership`() {
        assertTrue(topFiveRecommendedForwardDifferentials.all { player -> player.selectedByPercentage <= 10 })
    }

    @Test
    fun `top recommended differential in position is correct`() {
        assertEquals("Pukki", topFiveRecommendedForwardDifferentials.first().lastName)
    }

}