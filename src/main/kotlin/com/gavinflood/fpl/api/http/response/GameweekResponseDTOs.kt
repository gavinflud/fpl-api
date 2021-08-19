package com.gavinflood.fpl.api.http.response

data class GameweekResponse(
    val elements: List<GameweekResponsePlayerDetail>
) : DTO

data class GameweekResponsePlayerDetail(
    val id: Long,
    val stats: GameweekResponsePlayerStats,
    val explain: List<GameweekResponsePlayerExplanation>
)

data class GameweekResponsePlayerStats(
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
    val influence: Double,
    val creativity: Double,
    val threat: Double,
    val ict_index: Double,
    val total_points: Int,
    val in_dreamteam: Boolean
)

data class GameweekResponsePlayerExplanation(
    val fixture: Long,
    val stats: List<GameweekResponsePlayerExplanationStat>
)

data class GameweekResponsePlayerExplanationStat(
    val identifier: String,
    val points: Int,
    val value: Int
)