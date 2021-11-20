package com.gavinflood.fpl.api

import com.gavinflood.fpl.api.cache.ExpirableCache
import com.gavinflood.fpl.api.handlers.FixtureHandler
import com.gavinflood.fpl.api.handlers.GameWeekHandler
import com.gavinflood.fpl.api.handlers.PlayerHandler
import com.gavinflood.fpl.api.handlers.TeamHandler
import com.gavinflood.fpl.api.http.Client
import com.gavinflood.fpl.api.http.response.FixturesResponse
import com.gavinflood.fpl.api.http.response.GeneralResponse
import com.gavinflood.fpl.api.properties.FplProperties

/**
 * This is the primary entry-point to get Fantasy Football data. It handles the calls to the official Fantasy Football
 * API and parses the responses, returning the necessary data in more refined objects.
 */
object FantasyAPI {

    private const val generalInfoCacheKey = "GENERAL_INFO"
    private const val fixturesCacheKey = "FIXTURES"

    private val properties = FplProperties()
    private val client = Client()
    private val cache = ExpirableCache(properties.getCacheFlushIntervalInMillis())

    val teams = TeamHandler()
    val gameWeeks = GameWeekHandler()
    val players = PlayerHandler()
    val fixtures = FixtureHandler()

    /**
     * Get bootstrap data.
     */
    internal fun getGeneralInfo(): GeneralResponse = getFromCache(generalInfoCacheKey) { client.getGeneralInfo() }

    /**
     * Get fixture data.
     */
    internal fun getFixtures(): FixturesResponse = getFromCache(fixturesCacheKey) { client.getFixtures() }

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