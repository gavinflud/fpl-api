package com.gavinflood.fpl.api.http.response

import java.util.*

/**
 * Raw response data from the player summary endpoint.
 */
data class PlayerResponse(
    val fixtures: List<PlayerResponseFixture>,
    val history: List<PlayerResponseHistory>,
    val history_past: List<PlayerResponseHistoryPast>
) : DTO

data class PlayerResponseFixture(
    val id: Long,
    val code: Long,
    val team_h: Int,
    val team_h_score: Int,
    val team_a: Int,
    val team_a_score: Int,
    val event: Int,
    val finished: Boolean,
    val minutes: Int,
    val provisional_start_time: Boolean,
    val kickoff_time: Date,
    val event_name: String,
    val is_home: Boolean,
    val difficulty: Int
)

data class PlayerResponseHistory(
    val element: Long,
    val fixture: Long,
    val opponent_team: Int,
    val total_points: Int,
    val was_home: Boolean,
    val kickoff_time: Date,
    val team_h_score: Int,
    val team_a_score: Int,
    val round: Int,
    val minutes: Int,
    val goals_scored: Int,
    val assists: Int,
    val clean_sheets: Int,
    val goals_conceded: Int,
    val own_goals: Int,
    val penalties_saved: Int,
    val penalties_missed: Int,
    val yellow_cards: Int,
    val red_cards: Int,
    val saves: Int,
    val bonus: Int,
    val bps: Int,
    val influence: String,
    val creativity: String,
    val threat: String,
    val ict_index: String,
    val value: Int,
    val transfers_balance: Int,
    val selected: Long,
    val transfers_in: Long,
    val transfers_out: Long
)

data class PlayerResponseHistoryPast(
    val season_name: String,
    val element_code: Long,
    val start_cost: Int,
    val end_cost: Int,
    val total_points: Int,
    val minutes: Int,
    val goals_scored: Int,
    val assists: Int,
    val clean_sheets: Int,
    val goals_conceded: Int,
    val own_goals: Int,
    val penalties_saved: Int,
    val penalties_missed: Int,
    val yellow_cards: Int,
    val red_cards: Int,
    val saves: Int,
    val bonus: Int,
    val bps: Int,
    val influence: String,
    val creativity: String,
    val threat: String,
    val ict_index: String
)