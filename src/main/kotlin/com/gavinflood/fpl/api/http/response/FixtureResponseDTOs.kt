package com.gavinflood.fpl.api.http.response

import java.util.*

class FixturesResponse : ArrayList<FixturesResponseDetail>(), DTO

data class FixturesResponseDetail(
    val code: Long,
    val event: Int,
    val finished: Boolean,
    val finished_provisional: Boolean,
    val id: Long,
    val kickoff_time: Date,
    val minutes: Int,
    val provisional_start_time: Boolean,
    val started: Boolean,
    val team_a: Int,
    val team_a_score: Int,
    val team_h: Int,
    val team_h_score: Int,
    val stats: List<FixturesResponseStats>,
    val team_h_difficulty: Int,
    val team_a_difficulty: Int,
    val pulse_id: Long
) : DTO

data class FixturesResponseStats(
    val identifier: String,
    val a: List<FixturesResponseStat>,
    val h: List<FixturesResponseStat>
)

data class FixturesResponseStat(
    val value: Int,
    val element: Long
)