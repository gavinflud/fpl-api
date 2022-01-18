package com.gavinflood.fpl.api

import com.gavinflood.fpl.api.cache.ExpirableCache
import com.gavinflood.fpl.api.handlers.*
import com.gavinflood.fpl.api.http.Client
import org.slf4j.LoggerFactory

/**
 * This is the primary entry-point to get Fantasy Football data. It handles the calls to the official Fantasy Football
 * API and parses the responses, returning the necessary data in more refined objects.
 */
object FantasyAPI {

    private val logger = LoggerFactory.getLogger(FantasyAPI::class.java)

    val teams = TeamHandler()
    val gameWeeks = GameWeekHandler()
    val players = PlayerHandler()
    val fixtures = FixtureHandler()
    val managers = ManagerHandler()
    val leagues = LeagueHandler()

    /**
     * Provides direct access to the Fantasy Premier League's API endpoints. The functions you can call from this do not
     * perform any post-processing of the responses from the API, only mapping the JSON responses to plain objects.
     *
     * In general, you should avoid using this unless any of the functionality built into the handlers does not
     * accommodate your needs.
     */
    var httpClient = Client()

    /**
     * Updates the number of milliseconds it takes the cache to flush its contents to [flushInterval]. This will flush
     * the cache once called.
     */
    fun setCacheFlushIntervalInMillis(flushInterval: Long) {
        logger.info("Cache flush interval changed to ${flushInterval}ms")
        httpClient = Client(ExpirableCache(flushInterval))
    }

}