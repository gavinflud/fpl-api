package com.gavinflood.fpl.api.http.response

/**
 * Raw response data from the manager picks endpoint.
 */
data class ManagerPicksResponse(
    //val active_chip: String,
    val automatic_subs: List<ManagerPicksResponseAutomaticSub>,
    val entry_history: ManagerPicksResponseHistory,
    val picks: List<ManagerPicksResponsePick>
) : DTO

data class ManagerPicksResponseAutomaticSub(
    val entry: Long,
    val element_in: Int,
    val element_out: Int,
    val event: Int
)

data class ManagerPicksResponseHistory(
    val event: Int,
    val points: Int,
    val total_points: Int,
    val rank: Int,
    val rank_sort: Int,
    val overall_rank: Int,
    val bank: Int,
    val value: Int,
    val event_transfers: Int,
    val event_transfers_cost: Int,
    val points_on_bench: Int
)

data class ManagerPicksResponsePick(
    val element: Int,
    val position: Int,
    val multiplier: Int,
    val is_captain: Boolean,
    val is_vice_captain: Boolean
)