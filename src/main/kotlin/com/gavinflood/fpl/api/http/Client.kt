package com.gavinflood.fpl.api.http

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.gavinflood.fpl.api.cache.Cache
import com.gavinflood.fpl.api.cache.ExpirableCache
import com.gavinflood.fpl.api.http.response.*
import com.gavinflood.fpl.api.properties.FplProperties
import okhttp3.OkHttpClient
import okhttp3.Request
import org.slf4j.LoggerFactory
import java.io.IOException
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import kotlin.reflect.KClass

/**
 * HTTP client to call the fantasy football API and return parsed JSON responses.
 */
class Client(internal val cache: Cache = ExpirableCache()) {

    private val logger = LoggerFactory.getLogger(Client::class.java)
    private val properties = FplProperties()
    private val mapper = initializeMapper()
    private val okHttpClient = OkHttpClient()
    private val maxNumberOfRequestsPerMinute = 30
    private var numberOfRequestsInLastMinute = 0
    private var dateTimeOfLastRequestTrackingWindow = LocalDateTime.now()

    companion object {
        internal const val GENERAL_INFO_CACHE_KEY = "GENERAL_INFO"
        internal const val FIXTURES_CACHE_KEY = "FIXTURES"
        internal const val PLAYER_CACHE_KEY = "PLAYER"
        internal const val GAME_WEEK_CACHE_KEY = "GAME_WEEK"
        internal const val MANAGER_CACHE_KEY = "MANAGER"
        internal const val MANAGER_HISTORY_CACHE_KEY = "MANAGER_HISTORY"
        internal const val MANAGER_PICKS_CACHE_KEY = "MANAGER_PICKS"
        internal const val CLASSIC_LEAGUE_STANDINGS_CACHE_KEY = "CLASSIC_LEAGUE_STANDINGS"
    }

    /**
     * Get bootstrap data. This contains data on the teams, game-weeks, phases, players, and positions.
     */
    fun getGeneralInfo(): GeneralResponse =
        getFromCache(GENERAL_INFO_CACHE_KEY) {
            sendRequest(
                properties.getGeneralUrl(),
                GeneralResponse::class
            )
        }

    /**
     * Get fixture data.
     */
    fun getFixtures(): FixturesResponse =
        getFromCache(FIXTURES_CACHE_KEY) { sendRequest(properties.getFixturesUrl(), FixturesResponse::class) }

    /**
     * Get player data where [id] identifies the player.
     */
    fun getPlayer(id: Long): PlayerResponse =
        getFromCache("$PLAYER_CACHE_KEY:$id") { sendRequest(properties.getPlayersUrl(id), PlayerResponse::class) }

    /**
     * Get game-week data where [id] identifies the game-week.
     */
    fun getGameWeek(id: Int): GameweekResponse =
        getFromCache("$GAME_WEEK_CACHE_KEY:$id") { sendRequest(properties.getGameWeekUrl(id), GameweekResponse::class) }

    /**
     * Get manager data where [id] identifies the manager.
     */
    fun getManager(id: Long): ManagerResponse =
        getFromCache("$MANAGER_CACHE_KEY:$id") { sendRequest(properties.getManagerUrl(id), ManagerResponse::class) }

    /**
     * Get manager history data where [id] identifies the manager.
     */
    fun getManagerHistory(id: Long): ManagerHistoryResponse = getFromCache("$MANAGER_HISTORY_CACHE_KEY:$id") {
        sendRequest(
            properties.getManagerHistoryUrl(id),
            ManagerHistoryResponse::class
        )
    }

    /**
     * Get manager picks data where [id] identifies the manager.
     */
    fun getManagerPicks(id: Long, gameWeekNum: Int): ManagerPicksResponse =
        getFromCache("$MANAGER_PICKS_CACHE_KEY:$id:$gameWeekNum") {
            sendRequest(properties.getManagerPicksUrl(id, gameWeekNum), ManagerPicksResponse::class)
        }

    /**
     * Get classic league standings data where [id] identifies the league. The resulting "new_entries" and "standings"
     * objects are paginated and these pages can be specified with [newEntriesPage] and [standingsPage].
     */
    fun getClassicLeagueStandings(id: Long, newEntriesPage: Int, standingsPage: Int): ClassicLeagueResponse =
        getFromCache("$CLASSIC_LEAGUE_STANDINGS_CACHE_KEY:$id:$newEntriesPage:$standingsPage") {
            sendRequest(
                properties.getClassicLeagueStandingsUrl(id, newEntriesPage, standingsPage),
                ClassicLeagueResponse::class
            )
        }

    /**
     * Send an HTTP GET request to [url] and parse the response, returning an instance of [returnType].
     */
    internal fun <T : DTO> sendRequest(url: String, returnType: KClass<T>): T {
        if (!isRequestVolumeUnderThreshold()) {
            throw TooManyRequestsToServerException(maxNumberOfRequestsPerMinute)
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("User-Agent", properties.getUserAgentHeader())
            .build()

        numberOfRequestsInLastMinute++
        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected response code: ${response.code}")
            }

            return mapper.readValue(response.body!!.byteStream(), returnType.java)
        }
    }

    /**
     * Check if a value exists in the cache and if so return it. If it doesn't or if the cache has expired, use the
     * [retrieve] function to fetch the data and store it in the cache before returning it.
     */
    private fun <T> getFromCache(key: String, retrieve: () -> Any): T {
        val value = cache[key]

        if (value == null) {
            logger.debug("$key does not exist in cache. Retrieving it.")
            cache[key] = retrieve()
        } else {
            logger.debug("Retrieved $key from cache")
        }

        return cache[key] as T
    }

    /**
     * Check if more than 30 requests have been made in the past minute. This is used to limit the number of requests
     * to the API.
     */
    private fun isRequestVolumeUnderThreshold(): Boolean {
        val currentDateTime = LocalDateTime.now()
        if (dateTimeOfLastRequestTrackingWindow.until(currentDateTime, ChronoUnit.MINUTES) >= 1) {
            dateTimeOfLastRequestTrackingWindow = currentDateTime
            numberOfRequestsInLastMinute = 0
            return true
        }

        return numberOfRequestsInLastMinute < maxNumberOfRequestsPerMinute
    }

    /**
     * Initialize the Jackson Kotlin mapper. Failing on unknown properties is disabled so that any values in the
     * response from the API that don't have a corresponding DTO property don't cause a hard failure.
     */
    private fun initializeMapper(): ObjectMapper =
        ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

}

class TooManyRequestsToServerException(numRequests: Int) :
    Exception("More than $numRequests requests to the server in the past minute")