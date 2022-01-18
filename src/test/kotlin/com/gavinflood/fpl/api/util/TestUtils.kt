package com.gavinflood.fpl.api.util

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.gavinflood.fpl.api.http.ClientTest
import com.gavinflood.fpl.api.http.response.*

object TestUtils {

    private val mapper = initializeMapper()

    val generalResponse: GeneralResponse = loadPayload("bootstrap.json", GeneralResponse::class.java)
    val fixturesResponse: FixturesResponse = loadPayload("fixtures.json", FixturesResponse::class.java)
    val playerResponse: PlayerResponse = loadPayload("player.json", PlayerResponse::class.java)
    val gameWeekResponse: GameweekResponse = loadPayload("gameWeek.json", GameweekResponse::class.java)
    val managerResponse: ManagerResponse = loadPayload("manager.json", ManagerResponse::class.java)
    val manager2Response: ManagerResponse = loadPayload("manager2.json", ManagerResponse::class.java)
    val manager3Response: ManagerResponse = loadPayload("manager3.json", ManagerResponse::class.java)
    val manager4Response: ManagerResponse = loadPayload("manager4.json", ManagerResponse::class.java)
    val managerHistoryResponse: ManagerHistoryResponse =
        loadPayload("managerHistory.json", ManagerHistoryResponse::class.java)
    val managerHistory2Response: ManagerHistoryResponse =
        loadPayload("manager2History.json", ManagerHistoryResponse::class.java)
    val managerHistory3Response: ManagerHistoryResponse =
        loadPayload("manager3History.json", ManagerHistoryResponse::class.java)
    val managerHistory4Response: ManagerHistoryResponse =
        loadPayload("manager4History.json", ManagerHistoryResponse::class.java)
    val managerPicksResponse: ManagerPicksResponse = loadPayload("managerPicks.json", ManagerPicksResponse::class.java)
    val classicLeagueResponse: ClassicLeagueResponse =
        loadPayload("classicLeague.json", ClassicLeagueResponse::class.java)
    val classicLeague2Response: ClassicLeagueResponse =
        loadPayload("classicLeague2.json", ClassicLeagueResponse::class.java)
    val classicLeague4TeamsResponse: ClassicLeagueResponse =
        loadPayload("classicLeague4Teams.json", ClassicLeagueResponse::class.java)

    private fun <T> loadPayload(name: String, returnType: Class<T>) = mapper.readValue(
        ClientTest::class.java.classLoader.getResourceAsStream("payloads/$name"), returnType
    )

    private fun initializeMapper(): ObjectMapper =
        ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

}