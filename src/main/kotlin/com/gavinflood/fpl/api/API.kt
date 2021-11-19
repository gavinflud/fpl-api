package com.gavinflood.fpl.api

import com.gavinflood.fpl.api.handlers.FixtureHandler
import com.gavinflood.fpl.api.handlers.GameWeekHandler
import com.gavinflood.fpl.api.handlers.PlayerHandler
import com.gavinflood.fpl.api.handlers.TeamHandler
import com.gavinflood.fpl.api.http.Client
import com.gavinflood.fpl.api.mapper.Mapper

/**
 * This is the primary entry-point to get Fantasy Football data. It handles the calls to the official Fantasy Football
 * API and parses the responses, returning the necessary data in more refined objects.
 */
object API {

    private val client = Client()
    private val mapper = Mapper()

    val teams = TeamHandler()
    val gameWeeks = GameWeekHandler()
    val players = PlayerHandler()
    val fixtures = FixtureHandler()

    internal fun getGeneralInfo() = client.getGeneralInfo()

    internal fun getFixtures() = client.getFixtures()

}