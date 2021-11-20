package com.gavinflood.fpl.api.http.response

import java.time.LocalDateTime
import java.util.*

data class GeneralResponse(
    val events: List<GeneralResponseEvent>,
    val phases: List<GeneralResponsePhase>,
    val teams: List<GeneralResponseTeam>,
    val elements: List<GeneralResponsePlayer>,
    val element_types: List<GeneralResponsePlayerType>
) : DTO

data class GeneralResponseEvent(
    val id: Int,
    val name: String,
    val deadline_time: LocalDateTime,
    val average_entry_score: Int,
    val finished: Boolean,
    val highest_scoring_entry: Long,
    val highest_score: Int,
    val is_previous: Boolean,
    val is_current: Boolean,
    val is_next: Boolean,
    val chip_plays: List<GeneralResponseEventChipPlay>,
    val most_selected: Long,
    val most_transferred_in: Long,
    val top_element: Long,
    val transfers_made: Int,
    val most_captained: Int,
    val most_vice_captained: Int
)

data class GeneralResponseEventChipPlay(
    val chip_name: String,
    val num_played: Int
)

data class GeneralResponsePhase(
    val id: Long,
    val name: String,
    val start_event: Long,
    val stop_event: Long
)

data class GeneralResponseTeam(
    val code: Long,
    val draw: Int,
    val form: Int,
    val id: Int,
    val loss: Int,
    val name: String,
    val played: Int,
    val points: Int,
    val position: Int,
    val short_name: String,
    val strength: Int,
    val team_division: Int,
    val unavailable: Boolean,
    val win: Int,
    val strength_overall_home: Int,
    val strength_overall_away: Int,
    val strength_attack_home: Int,
    val strength_attack_away: Int,
    val strength_defence_home: Int,
    val strength_defence_away: Int,
    val pulse_id: Int
)

data class GeneralResponsePlayer(
    val chance_of_playing_next_round: Int,
    val chance_of_playing_this_round: Int,
    val code: Long,
    val cost_change_event: Int,
    val cost_change_event_fall: Int,
    val cost_change_start: Int,
    val cost_change_start_fall: Int,
    val dreamteam_count: Int,
    val element_type: Int,
    val ep_next: String,
    val ep_this: String,
    val event_points: Int,
    val first_name: String,
    val form: String,
    val id: Int,
    val in_dreamteam: Boolean,
    val news: String,
    val news_added: Date?,
    val now_cost: Int,
    val photo: String,
    val points_per_game: String,
    val second_name: String,
    val selected_by_percent: Double,
    val special: Boolean,
    val squad_number: Int,
    val status: String,
    val team: Int,
    val team_code: Long,
    val total_points: Int,
    val transfers_in: Int,
    val transfers_in_event: Int,
    val transfers_out: Int,
    val transfers_out_event: Int,
    val value_form: String,
    val value_season: String,
    val web_name: String,
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
    val influence_rank: Int,
    val influence_rank_type: Int,
    val creativity_rank: Int,
    val creativity_rank_type: Int,
    val threat_rank: Int,
    val threat_rank_type: Int,
    val ict_index_rank: Int,
    val ict_index_rank_type: Int,
    val corners_and_indirect_freekicks_order: Int,
    val corners_and_indirect_freekicks_text: String,
    val direct_freekicks_order: Int,
    val direct_freekicks_text: String,
    val penalties_order: Int,
    val penalties_text: String
)

data class GeneralResponsePlayerType(
    val id: Int,
    val plural_name_short: String,
    val squad_select: Int,
    val squad_min_play: Int,
    val squad_max_play: Int,
    val element_count: Int
)