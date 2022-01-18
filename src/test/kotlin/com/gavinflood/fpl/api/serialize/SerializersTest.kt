package com.gavinflood.fpl.api.serialize

import com.gavinflood.fpl.api.domain.GameWeek
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SerializersTest {

    @Nested
    inner class LocalDateTimeSerializerTest {

        private val gameWeek = GameWeek(
            1, "Gameweek 1", 50, 100,
            isCurrent = true,
            isNext = false,
            isFinished = false,
            emptyList(),
            1000,
            LocalDateTime.of(LocalDate.now().plusDays(5), LocalTime.MIDNIGHT),
            mostCaptainedPlayer = null,
            mostViceCaptainedPlayer = null
        )

        @Test
        fun `serializing GameWeek with LocalDateTime value is successful`() {
            val gameWeekString = Json.encodeToString(gameWeek)
            assertTrue(gameWeekString.contains(gameWeek.deadline.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)))
        }

        @Test
        fun `deserializing GameWeek with LocalDateTime value is successful`() {
            val gameWeekString = Json.encodeToString(gameWeek)
            val deserializedGameWeek = Json.decodeFromString<GameWeek>(gameWeekString)
            assertEquals(gameWeek.deadline, deserializedGameWeek.deadline)
        }

        @Test
        fun `description of LocalDateTime serializer is correct`() {
            assertEquals("LocalDateTime", LocalDateTimeSerializer().descriptor.serialName)
        }
    }

}