package com.gavinflood.fpl.api.http.response

data class ManagerHistoryResponse(
    val current: List<ManagerHistoryResponseCurrentDetail>,
    val past: List<ManagerHistoryResponsePastDetail>
) : DTO

data class ManagerHistoryResponseCurrentDetail(
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

data class ManagerHistoryResponsePastDetail(
    val season_name: String,
    val total_points: Int,
    val rank: Int
)