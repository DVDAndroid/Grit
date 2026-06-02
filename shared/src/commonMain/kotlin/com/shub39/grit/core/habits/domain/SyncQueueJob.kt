package com.shub39.grit.core.habits.domain

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SyncQueueJob(
    val id: Long,
    val operation: SyncQueueOperations,
    val payload: String,
)

@Serializable
enum class SyncQueueOperations {
    UPDATE_HABIT,
    DELETE_HABIT,
    UPDATE_HABIT_STATUS,
    DELETE_HABIT_STATUS;
}

@Serializable
data class HabitStatusMinimalKeys(
    val habitId: Long,
    val date: LocalDate,
)