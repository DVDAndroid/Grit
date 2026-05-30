/*
 * Copyright (C) 2026  Shubham Gorai
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.shub39.grit.habits.data

import com.shub39.grit.core.habits.domain.Habit
import com.shub39.grit.core.habits.domain.HabitStatus
import com.shub39.grit.core.now
import com.shub39.grit.habits.data.database.HabitEntity
import com.shub39.grit.habits.data.database.HabitStatusEntity
import kotlinx.datetime.LocalDateTime

fun HabitEntity.toHabit(): Habit {
    return Habit(
        id = id,
        title = title,
        description = description,
        time = time,
        days = days,
        index = index,
        reminder = reminder,
        type = type,
        updatedAt = updatedAt,
    )
}

fun HabitStatusEntity.toHabitStatus(): HabitStatus {
    return HabitStatus(id = id, habitId = habitId, date = date, ok = ok, notes = notes, numberValue = numberValue, updatedAt = updatedAt)
}

fun Habit.toHabitEntity(): HabitEntity {
    return HabitEntity(
        id = id,
        title = title,
        description = description,
        time = time,
        index = index,
        days = days,
        reminder = reminder,
        type = type,
        updatedAt = LocalDateTime.now(),
    )
}

fun HabitStatus.toHabitStatusEntity(): HabitStatusEntity {
    return HabitStatusEntity(id = id, habitId = habitId, date = date, ok = ok, notes = notes, numberValue = numberValue, updatedAt = LocalDateTime.now())
}
