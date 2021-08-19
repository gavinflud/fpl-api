package com.gavinflood.fpl.api

import com.gavinflood.fpl.api.http.Client

fun main() {
    val client = Client()
    val generalInfo = client.getGeneralInfo()
    val fixtureInfo = client.getFixtures()
    val player = client.getPlayer(233)
    val gameWeek = client.getGameWeek(1)
    val manager = client.getManager(3573238)
    val managerHistory = client.getManagerHistory(3573238)
    val getManagerPicks = client.getManagerPicks(3573238, 1)
    val classicLeague = client.getClassicLeagueStandings(12603, 1, 1)
    print(generalInfo)
}