package com.gavinflood.fpl.api.http

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.gavinflood.fpl.api.http.response.*
import com.gavinflood.fpl.api.properties.FplProperties
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.reflect.KClass

/**
 * HTTP client to call the fantasy football API and return parsed JSON responses.
 */
class Client() {

    private val properties = FplProperties()
    private val httpClient = OkHttpClient()
    private val mapper = initializeMapper()

    /**
     * Get bootstrap data.
     */
    fun getGeneralInfo() = sendRequest(properties.getGeneralUrl(), GeneralResponse::class)

    /**
     * Get fixture data.
     */
    fun getFixtures() = sendRequest(properties.getFixturesUrl(), FixturesResponse::class)

    /**
     * Get player data where [id] identifies the player.
     */
    fun getPlayer(id: Long) = sendRequest(properties.getPlayersUrl(id), PlayerResponse::class)

    /**
     * Get game-week data where [id] identifies the game-week.
     */
    fun getGameWeek(id: Int) = sendRequest(properties.getGameWeekUrl(id), GameweekResponse::class)

    /**
     * Get manager data where [id] identifies the manager.
     */
    fun getManager(id: Long) = sendRequest(properties.getManagerUrl(id), ManagerResponse::class)

    /**
     * Get manager history data where [id] identifies the manager.
     */
    fun getManagerHistory(id: Long) = sendRequest(properties.getManagerHistoryUrl(id), ManagerHistoryResponse::class)

    /**
     * Get manager picks data where [id] identifies the manager.
     */
    fun getManagerPicks(id: Long, gameWeekNum: Int) =
        sendRequest(properties.getManagerPicksUrl(id, gameWeekNum), ManagerPicksResponse::class)

    /**
     * Get classic league standings data where [id] identifies the league. The resulting "new_entries" and "standings"
     * objects are paginated and these pages can be specified with [newEntriesPage] and [standingsPage].
     */
    fun getClassicLeagueStandings(id: Long, newEntriesPage: Int, standingsPage: Int) = sendRequest(
        properties.getClassicLeagueStandingsUrl(id, newEntriesPage, standingsPage),
        ClassicLeagueResponse::class
    )

    /**
     * Send an HTTP GET request to [url] and parse the response, returning an instance of [returnType].
     */
    private fun <T : DTO> sendRequest(url: String, returnType: KClass<T>): T {
        val request = Request.Builder()
            .url(url)
            .addHeader("User-Agent", properties.getUserAgentHeader())
            .build()

        httpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected response code: ${response.code}")

            return mapper.readValue(response.body!!.byteStream(), returnType.java)
        }
    }

    /**
     * Initialize the Jackson Kotlin mapper. Failing on unknown properties is disabled so that any values in the
     * response from the API that don't have a corresponding DTO property don't cause a hard failure.
     */
    private fun initializeMapper(): ObjectMapper =
        ObjectMapper().registerKotlinModule().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

}