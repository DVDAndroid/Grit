package com.shub39.grit.core.habits.domain

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class HabitCompletion(val ok: Int)

val HabitCompleted = HabitCompletion(1)
val HabitOnlyNotes = HabitCompletion(0)