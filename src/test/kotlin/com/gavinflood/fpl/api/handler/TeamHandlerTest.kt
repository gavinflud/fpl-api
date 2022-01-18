package com.gavinflood.fpl.api.handler

import com.gavinflood.fpl.api.FantasyAPI
import com.gavinflood.fpl.api.handlers.TeamHandler
import com.gavinflood.fpl.api.http.Client
import com.gavinflood.fpl.api.http.response.GeneralResponse
import com.gavinflood.fpl.api.util.TestUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.spy
import org.mockito.kotlin.whenever

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeamHandlerTest : HandlerTest() {

    @BeforeAll
    fun init() {
        val mockClient = spy<Client>()

        doReturn(TestUtils.generalResponse)
            .whenever(mockClient)
            .sendRequest(properties.getGeneralUrl(), GeneralResponse::class)

        FantasyAPI.httpClient = mockClient
    }

    @Test
    fun `all teams are returned from get`() {
        assertEquals(20, TeamHandler().get().size)
    }

    @Test
    fun `correct team is returned from get by ID`() {
        assertEquals("Burnley", TeamHandler().get(5).name)
    }

    @Test
    fun `exception thrown when calling get with an invalid ID`() {
        assertThrows<NoSuchElementException> { TeamHandler().get(21) }
    }

}