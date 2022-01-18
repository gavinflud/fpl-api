package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManagerTest {

    @Test
    fun `manager is successfully serialized`() {
        val manager = Manager(123, "Joe", "Bloggs", "Test Team", emptyList())
        Assertions.assertTrue(Json.encodeToString(manager).contains(manager.lastName))
    }

    @Test
    fun `manager is successfully deserialized`() {
        val manager = Manager(123, "Joe", "Bloggs", "Test Team", emptyList())
        val json = Json.encodeToString(manager)
        Assertions.assertEquals(manager.id, Json.decodeFromString<Manager>(json).id)
    }

}