package com.shub39.grit.core.habits.domain

import kotlinx.serialization.Serializable

@Serializable
enum class HabitType(val id: String) {
    Boolean("boolean"),
    Numeric("numeric");

    companion object {
        fun byValue(id: String) = entries.single { it.id == id }
    }
}