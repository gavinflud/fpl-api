package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManagerHistoryEntryTest {

    @Test
    fun `manager history entry is successfully serialized`() {
        val entry = ManagerHistoryEntry(createGameWeek(), 20, 100, 2, 12, 1000, 1, 0, 4)
        Assertions.assertTrue(Json.encodeToString(entry).contains(entry.moneyInBank.toString()))
    }

    @Test
    fun `manager history entry is successfully deserialized`() {
        val entry = ManagerHistoryEntry(createGameWeek(), 20, 100, 2, 12, 1000, 1, 0, 4)
        val json = Json.encodeToString(entry)
        Assertions.assertEquals(entry.pointsOnBench, Json.decodeFromString<ManagerHistoryEntry>(json).pointsOnBench)
    }

    private fun createGameWeek() = GameWeek(
        1,
        "Week 1",
        50,
        100,
        isCurrent = true,
        isNext = false,
        isFinished = false,
        emptyList(),
        1000,
        LocalDateTime.now().minusDays(1),
        null,
        null
    )

}