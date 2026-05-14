package com.shub39.grit.core.habits.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class HabitType(val type: String)

val HabitBoolean = HabitType("boolean")
val HabitNumber = HabitType("number")