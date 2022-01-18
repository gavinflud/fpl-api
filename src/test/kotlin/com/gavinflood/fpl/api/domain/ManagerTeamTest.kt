package com.gavinflood.fpl.api.domain

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ManagerTeamTest {

    @Test
    fun `manager team is successfully serialized`() {
        assertTrue(Json.encodeToString(createManagerTeam()).contains("Player 9"))
    }

    @Test
    fun `manager team is successfully deserialized`() {
        val json = Json.encodeToString(createManagerTeam())
        assertTrue(Json.decodeFromString<ManagerTeam>(json).players[1].isCaptain)
    }

    private fun createManagerTeam(): ManagerTeam {
        val managerPlayers = mutableListOf<ManagerPlayer>()

        for (i in 1..15) {
            val team = createTeam(i, "Team $i")
            val player = createPlayer(i, "Joe", "Player $i", team, Position.getEnumById(Random.nextInt(1, 5)))
            managerPlayers.add(ManagerPlayer(player, i, isCaptain = i == 2, isViceCaptain = i == 4))
        }

        return ManagerTeam(managerPlayers)
    }

    private fun createTeam(id: Int, name: String): Team {
        return Team(id, name, 1000, 1000, 1000, 1000, 1000, 1000)
    }

    private fun createPlayer(id: Int, firstName: String, lastName: String, team: Team, position: Position): Player {
        return Player(id, firstName, lastName, team, position, 0, false, 6.0, 4.5, 10.2, 90, 1080, 2000, 1000, 100)
    }

}