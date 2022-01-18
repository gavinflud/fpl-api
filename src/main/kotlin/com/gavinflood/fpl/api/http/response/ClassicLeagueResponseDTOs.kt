package com.gavinflood.fpl.api.http.response

import java.time.LocalDateTime
import java.util.*

/**
 * Raw response data from the classic league standings endpoint.
 */
data class ClassicLeagueResponse(
    val new_entries: ClassicLeagueResponseNewEntries,
    val last_updated_data: Date,
    val league: ClassicLeagueResponseInfo,
    val standings: ClassicLeagueResponseStandings
) : DTO

data class ClassicLeagueResponseNewEntries(
    val has_next: Boolean,
    val page: Int,
    val results: List<ClassicLeagueResponseNewEntry>
)

data class ClassicLeagueResponseNewEntry(
    val entry: Long,
    val entry_name: String,
    val joined_time: Date,
    val player_first_name: String,
    val player_last_name: String
)

data class ClassicLeagueResponseInfo(
    val id: Long,
    val name: String,
    val created: LocalDateTime,
    val closed: Boolean,
    val max_entries: Int,
    val league_type: String,
    val scoring: String,
    val admin_entry: Long,
    val start_event: Int,
    val code_privacy: String,
    val has_cup: Boolean,
    val cup_league: Long,
    val rank: Int,
)

data class ClassicLeagueResponseStandings(
    val has_next: Boolean,
    val page: Int,
    val results: List<ClassicLeagueResponseResult>
)

data class ClassicLeagueResponseResult(
    val id: Long,
    val event_total: Int,
    val player_name: String,
    val rank: Int,
    val last_rank: Int,
    val rank_sort: Int,
    val total: Int,
    val entry: Long,
    val entry_name: String
)