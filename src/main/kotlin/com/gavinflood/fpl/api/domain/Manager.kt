package com.gavinflood.fpl.api.domain

import kotlinx.serialization.Serializable

/**
 * Represents a manager of a fantasy football team.
 */
@Serializable
class Manager(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val teamName: String,
    val history: List<ManagerHistoryEntry>
)