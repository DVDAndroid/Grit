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
package com.shub39.grit.habits.data.database

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.ForeignKey
import androidx.room3.Index
import androidx.room3.PrimaryKey
import com.shub39.grit.core.habits.domain.HabitCompletion
import com.shub39.grit.core.habits.domain.HabitCompletionSerializer
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Entity(
    tableName = "habit_status",
    foreignKeys =
        [
            ForeignKey(
                entity = HabitEntity::class,
                parentColumns = ["id"],
                childColumns = ["habitId"],
                onDelete = ForeignKey.CASCADE,
            )
        ],
    indices = [
        Index(value = ["habitId", "date"], unique = true)
    ],
)
@Serializable
data class HabitStatusEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val habitId: Long,
    val date: LocalDate,
    @ColumnInfo(
        name = "ok",
        defaultValue = "1",
        typeAffinity = ColumnInfo.INTEGER,
    )
    @Serializable(with = HabitCompletionSerializer::class)
    val ok: HabitCompletion = HabitCompletion.Completed,
    val notes: String? = null,
    val numberValue: Float? = null,
    @ColumnInfo(
        name = "updated_at",
        defaultValue = "CURRENT_TIMESTAMP",
    )
    val updatedAt: Instant,
)
