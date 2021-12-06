package com.gavinflood.fpl.api

import com.gavinflood.fpl.api.cache.ExpirableCache
import com.gavinflood.fpl.api.handlers.*
import com.gavinflood.fpl.api.http.Client
import com.gavinflood.fpl.api.http.response.FixturesResponse
import com.gavinflood.fpl.api.http.response.GeneralResponse
import com.gavinflood.fpl.api.http.response.ManagerPicksResponse
import com.gavinflood.fpl.api.properties.FplProperties

/**
 * This is the primary entry-point to get Fantasy Football data. It handles the calls to the official Fantasy Football
 * API and parses the responses, returning the necessary data in more refined objects.
 */
object FantasyAPI {

    private const val generalInfoCacheKey = "GENERAL_INFO"
    private const val fixturesCacheKey = "FIXTURES"
    private const val managerPicksCacheKey = "MANAGER_PICKS:"

    private val properties = FplProperties()
    private var cache = ExpirableCache(properties.getCacheFlushIntervalInMillis())

    val teams = TeamHandler()
    val gameWeeks = GameWeekHandler()
    val players = PlayerHandler()
    val fixtures = FixtureHandler()
    val managers = ManagerHandler()

    /**
     * Provides direct access to the Fantasy Premier League's API endpoints. The functions you can call from this do not
     * perform any post-processing of the responses from the API, only mapping the JSON responses to plain objects.
     *
     * In general, you should avoid using this unless any of the functionality built into the handlers does not
     * accommodate your needs.
     */
    val httpClient = Client()

    /**
     * Updates the number of milliseconds it takes the cache to flush its contents to [flushInterval]. This will flush
     * the cache once called.
     */
    fun setCacheFlushIntervalInMillis(flushInterval: Long) {
        cache = ExpirableCache(flushInterval)
    }

    /**
     * Get bootstrap data.
     */
    internal fun getGeneralInfo(): GeneralResponse = getFromCache(generalInfoCacheKey) { httpClient.getGeneralInfo() }

    /**
     * Get fixture data.
     */
    internal fun getFixtures(): FixturesResponse = getFromCache(fixturesCacheKey) { httpClient.getFixtures() }

    /**
     * Get manager picks data for a specific [gameWeekNum].
     */
    internal fun getManagerPicks(managerId: Long, gameWeekNum: Int): ManagerPicksResponse =
        getFromCache("$managerPicksCacheKey$managerId") { httpClient.getManagerPicks(managerId, gameWeekNum) }

    /**
     * Check if a value exists in the cache and if so return it. If it doesn't or if the cache has expired, use the
     * [retrieve] function to fetch the data and store it in the cache before returning it.
     */
    private fun <T> getFromCache(key: String, retrieve: () -> Any): T {
        val value = cache[key]

        if (value == null) {
            cache[key] = retrieve()
        }

        return cache[key] as T
    }

}