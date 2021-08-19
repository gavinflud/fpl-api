package com.gavinflood.fpl.api.http.response

import java.util.*

data class ManagerResponse(
    val id: Long,
    val joined_time: Date,
    val started_event: Int,
    val favourite_team: Int,
    val player_first_name: String,
    val player_last_name: String,
    val player_region_id: Int,
    val player_region_name: String,
    val player_region_iso_code_short: String,
    val player_region_iso_code_long: String,
    val summary_overall_points: Int,
    val summary_overall_rank: Int,
    val summary_event_points: Int,
    val summary_event_rank: Int,
    val current_event: Int,
    val leagues: ManagerResponseLeagues,
    val name: String,
    val name_change_blocked: Boolean,
    val kit: String,
    val last_deadline_bank: Int,
    val last_deadline_value: Int,
    val last_deadline_total_transfers: Int,
) : DTO

data class ManagerResponseLeagues(
    val classic: List<ManagerResponseLeague>,
    val h2h: List<ManagerResponseLeague>
)

data class ManagerResponseLeague(
    val id: Long,
    val name: String,
    val short_name: String?,
    val created: Date,
    val closed: Boolean,
    val rank: Int,
    val max_entries: Int,
    val league_type: String,
    val scoring: String,
    val admin_entry: Int,
    val start_event: Int,
    val entry_can_leave: Boolean,
    val entry_can_admin: Boolean,
    val entry_can_invite: Boolean,
    val has_cup: Boolean,
    val cup_league: Long,
    val cup_qualified: Long,
    val entry_rank: Int,
    val entry_last_rank: Int
)