package com.shub39.grit.core.habits.domain

import kotlinx.serialization.Serializable

@Serializable
enum class HabitCompletion(val ok: Int) {
    Completed(1),
    OnlyNotes(0);

    companion object {
        fun byValue(x: Int) = entries.single { it.ok == x }
    }
}