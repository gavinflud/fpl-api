package com.gavinflood.fpl.api.properties

import java.util.*
import java.util.regex.Pattern

/**
 * Wrapper for custom application properties.
 */
class FplProperties {

    private val properties = Properties()

    init {
        val inputStream = FplProperties::class.java.classLoader.getResourceAsStream("fpl.properties")
        properties.load(inputStream)
    }

    /**
     * Bootstrap data URL.
     */
    fun getGeneralUrl() = combineWithBaseUrl(properties.getProperty("http.general"))

    /**
     * Fixture data URL.
     */
    fun getFixturesUrl() = combineWithBaseUrl(properties.getProperty("http.fixtures"))

    /**
     * Individual player data URL where the player is identified by their [id].
     */
    fun getPlayersUrl(id: Long) = populateAndCombineWithBaseUrl(properties.getProperty("http.player"), id)

    /**
     * Individual game-week data URL where the game-week is identified by its [id].
     */
    fun getGameWeekUrl(id: Int) = populateAndCombineWithBaseUrl(properties.getProperty("http.game-week"), id)

    /**
     * Individual manager data URL where the manager is identified by their [id].
     */
    fun getManagerUrl(id: Long) = populateAndCombineWithBaseUrl(properties.getProperty("http.manager"), id)

    /**
     * Individual manager history data URL where the manager is identified by their [id].
     */
    fun getManagerHistoryUrl(id: Long) =
        populateAndCombineWithBaseUrl(properties.getProperty("http.manager.history"), id)

    /**
     * Individual manager transfer data URL where the manager is identified by their [id].
     */
    fun getManagerTransfersUrl(id: Long) =
        populateAndCombineWithBaseUrl(properties.getProperty("http.manager.transfers"), id)

    /**
     * Individual manager pick data URL where the manager is identified by their [id].
     */
    fun getManagerPicksUrl(id: Long, gameWeekNum: Int) =
        populateAndCombineWithBaseUrl(properties.getProperty("http.manager.picks"), id, gameWeekNum)

    /**
     * Classic league standings data URL where the league is identified by its [id]. The resulting "new_entries" and
     * "standings" objects are paginated and these pages can be specified with [newEntriesPage] and [standingsPage].
     */
    fun getClassicLeagueStandingsUrl(id: Long, newEntriesPage: Int = 1, standingsPage: Int = 1): String {
        val url = populateAndCombineWithBaseUrl(properties.getProperty("http.league.classic.standings"), id)
        val params = mapOf(
            Pair("page_new_entries", newEntriesPage.toString()),
            Pair("page_standings", standingsPage.toString())
        )
        return addUrlParams(url, params)
    }

    /**
     * User-Agent header value to be passed as part of HTTP headers.
     */
    fun getUserAgentHeader(): String = properties.getProperty("http.headers.user-agent")

    /**
     * The number of seconds that data will live in the cache for before it is cleared and fresh requests are made to
     * the server.
     */
    fun getCacheFlushIntervalInMillis(): Long = properties.getProperty("cache.flush-interval").toLong()

    /**
     * Appends [url] to the base fantasy football API url.
     */
    private fun combineWithBaseUrl(url: String) = properties.getProperty("http.base") + url

    /**
     * Populates dynamic parts of [url] with the values in [pathVars]. Dynamic parts are identified by "{}" and will
     * be populated in the order they appear in the URL.
     */
    private fun populateUrl(url: String, vararg pathVars: Any): String {
        val matcher = Pattern.compile("\\{}").matcher(url)
        val newUrl = StringBuilder()
        var i = 0

        while (matcher.find()) {
            matcher.appendReplacement(newUrl, pathVars[i].toString())
            i++
        }

        matcher.appendTail(newUrl)
        return newUrl.toString()
    }

    /**
     * Helper function to call both [combineWithBaseUrl] and pass the value to [populateUrl].
     */
    private fun populateAndCombineWithBaseUrl(url: String, vararg pathVars: Any) =
        populateUrl(combineWithBaseUrl(url), *pathVars)

    /**
     * Add query params to [url] using the [params] map. These will be in the format "?key1=value1&key2=value2".
     */
    private fun addUrlParams(url: String, params: Map<String, String>): String {
        return StringBuilder(url)
            .append("?")
            .append(params.map { entry -> "${entry.key}=${entry.value}" }.joinToString("&"))
            .toString()
    }

}