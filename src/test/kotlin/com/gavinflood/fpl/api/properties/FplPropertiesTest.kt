package com.gavinflood.fpl.api.properties

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
internal class FplPropertiesTest {

    private val properties = Properties()
    private val fplProperties = FplProperties()
    private val baseUrlProperty = "http.base"

    @BeforeAll
    private fun beforeAll() {
        val inputStream = FplProperties::class.java.classLoader.getResourceAsStream("fpl.properties")
        properties.load(inputStream)
    }

    @Test
    fun `getGeneralUrl combines base URL and bootstrap path`() = assertEquals(
        properties.getProperty(baseUrlProperty) + properties.getProperty("http.general"),
        fplProperties.getGeneralUrl()
    )

    @Test
    fun `getFixturesUrl combines base URL and fixtures path`() = assertEquals(
        properties.getProperty(baseUrlProperty) + properties.getProperty("http.fixtures"),
        fplProperties.getFixturesUrl()
    )

    @Test
    fun `getPlayersUrl combines base URL, players path, and ID`() {
        val id = 123456L
        val expected = properties.getProperty(baseUrlProperty) + "/element-summary/$id/"
        assertEquals(expected, fplProperties.getPlayersUrl(id))
    }

    @Test
    fun `getGameWeekUrl combines base URL, game-week path, and game-week`() {
        val gameWeek = 1
        val expected = properties.getProperty(baseUrlProperty) + "/event/$gameWeek/live/"
        assertEquals(expected, fplProperties.getGameWeekUrl(gameWeek))
    }

    @Test
    fun `getManagerUrl combines base URL, manager path, and ID`() {
        val id = 123456L
        val expected = properties.getProperty(baseUrlProperty) + "/entry/$id/"
        assertEquals(expected, fplProperties.getManagerUrl(id))
    }

    @Test
    fun `getManagerHistoryUrl combines base URL, manager history path, and ID`() {
        val id = 123456L
        val expected = properties.getProperty(baseUrlProperty) + "/entry/$id/history/"
        assertEquals(expected, fplProperties.getManagerHistoryUrl(id))
    }

    @Test
    fun `getManagerTransfersUrl combines base URL, transfers path, and ID`() {
        val id = 123456L
        val expected = properties.getProperty(baseUrlProperty) + "/entry/$id/transfers/"
        assertEquals(expected, fplProperties.getManagerTransfersUrl(id))
    }

    @Test
    fun `getManagerPicksUrl combines base URL, manager picks path, ID, and game-week`() {
        val id = 123456L
        val gameWeek = 1
        val expected = properties.getProperty(baseUrlProperty) + "/entry/$id/event/$gameWeek/picks/"
        assertEquals(expected, fplProperties.getManagerPicksUrl(id, gameWeek))
    }

    @Test
    fun `getClassicLeagueStandingsUrl combines base URL, classic league path, and ID`() {
        val id = 123456L
        val expected = properties.getProperty(baseUrlProperty) +
            "/leagues-classic/$id/standings/?page_new_entries=1&page_standings=1"
        assertEquals(expected, fplProperties.getClassicLeagueStandingsUrl(id))
    }

    @Test
    fun `getClassicLeagueStandingsUrlWithPagination combines base URL, classic league path, ID, and pagination`() {
        val id = 123456L
        val newEntriesPage = 3
        val standingsPage = 2
        val expected = properties.getProperty(baseUrlProperty) +
            "/leagues-classic/$id/standings/?page_new_entries=$newEntriesPage&page_standings=$standingsPage"
        assertEquals(expected, fplProperties.getClassicLeagueStandingsUrl(id, newEntriesPage, standingsPage))
    }

    @Test
    fun `getUserAgentHeader returns the user-agent header value in properties file`() =
        assertEquals(properties.getProperty("http.headers.user-agent"), fplProperties.getUserAgentHeader())

    @Test
    fun `getCacheFlushIntervalInMillis returns the cache flush interval value in properties file`() =
        assertEquals(
            properties.getProperty("cache.flush-interval").toLong(),
            fplProperties.getCacheFlushIntervalInMillis()
        )
}