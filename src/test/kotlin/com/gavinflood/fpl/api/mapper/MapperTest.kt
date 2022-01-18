package com.gavinflood.fpl.api.mapper

import com.gavinflood.fpl.api.domain.StatType
import com.gavinflood.fpl.api.handlers.findGameWeekById
import com.gavinflood.fpl.api.handlers.findPlayerById
import com.gavinflood.fpl.api.handlers.findTeamById
import com.gavinflood.fpl.api.util.TestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MapperTest {

    private val mapper = Mapper()

    @Test
    fun `game week is correctly mapped from general response`() {
        val generalInfo = TestUtils.generalResponse
        val gameWeek = mapper.mapGameWeek(generalInfo.events[3], findPlayerById(generalInfo))
        val salah = findPlayerById(generalInfo).invoke(233)
        assertEquals(salah!!.id, gameWeek.mostCaptainedPlayer!!.id)
    }

    @Test
    fun `triple captain chip play total is correctly mapped from general response`() {
        val generalInfo = TestUtils.generalResponse
        val tripleCaptainChipPlay = mapper.mapChipPlayTotal(generalInfo.events[0].chip_plays[1])
        assertEquals(225749, tripleCaptainChipPlay.numPlayed)
    }

    @Test
    fun `team is correctly mapped from general response`() {
        val generalInfo = TestUtils.generalResponse
        val leicester = mapper.mapTeam(generalInfo.teams.single { it.id == 9 })
        assertEquals("Leicester", leicester.name)
    }

    @Test
    fun `player is correctly mapped from general response`() {
        val generalInfo = TestUtils.generalResponse
        val bowen = mapper.mapPlayer(generalInfo.elements.single { it.id == 420 }, findTeamById(generalInfo))
        assertEquals("Bowen", bowen.lastName)
        assertEquals(3.2, bowen.form)
    }

    @Test
    fun `fixture is correctly mapped from fixtures response`() {
        val generalInfo = TestUtils.generalResponse
        val fixturesResponse = TestUtils.fixturesResponse
        val fixture = mapper.mapFixture(
            fixturesResponse[8],
            findGameWeekById(generalInfo),
            findTeamById(generalInfo),
            findPlayerById(generalInfo)
        )
        assertEquals(4, fixture.awayTeam.id)
        assertTrue(fixture.stats.any { it.type == StatType.GOAL && it.player.id == 98 })
    }

    @Test
    fun `manager history entries are correctly mapped from manager history response`() {
        val managerHistory = TestUtils.managerHistoryResponse
        val entries = mapper.mapManagerHistory(managerHistory, findGameWeekById(TestUtils.generalResponse))
        assertEquals(83, entries.single { it.gameWeek.id == 16 }.points)
    }

    @Test
    fun `manager is correctly mapped from manager response`() {
        val managerHistory = TestUtils.managerHistoryResponse
        val entries = mapper.mapManagerHistory(managerHistory, findGameWeekById(TestUtils.generalResponse))
        val manager = mapper.mapManager(TestUtils.managerResponse, entries)
        assertEquals("Show me the Mané", manager.teamName)
    }

    @Test
    fun `manager team is correctly mapped from manager picks response`() {
        val managerPicks = TestUtils.managerPicksResponse
        val findPlayerByIdNonNull = { id: Int -> findPlayerById(TestUtils.generalResponse).invoke(id)!! }
        val managerTeam = mapper.mapManagerTeam(managerPicks, findPlayerByIdNonNull)
        assertEquals(233, managerTeam.players.single { it.isCaptain }.player.id)
    }

}