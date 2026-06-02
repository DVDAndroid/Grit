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
package com.shub39.grit.core.data

import androidx.room3.TypeConverter
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.shub39.grit.core.habits.domain.HabitCompletion
import com.shub39.grit.core.habits.domain.HabitType
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object Converters {
    val allDays = dayOfWeekToString(DayOfWeek.entries.toSet())

    @TypeConverter
    fun dayOfWeekToString(value: Set<DayOfWeek>): String {
        return value.joinToString(",") { it.isoDayNumber.toString() }
    }

    @TypeConverter
    fun dayOfWeekFromString(value: String): Set<DayOfWeek> {
        return if (value.isBlank()) emptySet()
        else value.split(",").map { DayOfWeek(it.toInt()) }.toSet()
    }

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun dateFromTimestamp(value: Long?): LocalDateTime? {
        return value?.let {
            Instant.fromEpochSeconds(value).toLocalDateTime(TimeZone.currentSystemDefault())
        }
    }

    @OptIn(ExperimentalTime::class)
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?): Long? {
        return date?.toInstant(TimeZone.currentSystemDefault())?.epochSeconds
    }

    @TypeConverter
    fun dateStringToDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    @TypeConverter
    fun dateToDateString(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun fromInstant(value: Instant?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun stringToInstant(value: String?): Instant? {
        return value?.let { Instant.parse(it) }
    }

    @TypeConverter
    fun longToInstant(value: Long?): Instant? {
        return value?.let { Instant.fromEpochMilliseconds(value) }
    }

    @TypeConverter
    fun fromHabitCompletion(value: HabitCompletion): Int = value.ok

    @TypeConverter
    fun toHabitCompletion(value: Int): HabitCompletion = HabitCompletion.byValue(value)

    @TypeConverter
    fun fromHabitType(value: HabitType): String = value.id

    @TypeConverter
    fun toHabitType(value: String): HabitType = HabitType.byValue(value)
}

object InstantTypeAdapter : TypeAdapter<Instant>() {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

    override fun write(out: JsonWriter, value: Instant?) {
        if (value == null) {
            out.nullValue()
            return
        }

        val ldt = java.time.LocalDateTime.ofInstant(
            java.time.Instant.ofEpochMilli(value.toEpochMilliseconds()),
            ZoneOffset.UTC
        )

        out.value(formatter.format(ldt))
    }

    override fun read(reader: JsonReader): Instant? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }

        val text = reader.nextString()
        val ldt = java.time.LocalDateTime.parse(text, formatter)

        val epochMillis = ldt.toInstant(ZoneOffset.UTC).toEpochMilli()

        return Instant.DISTANT_PAST + epochMillis.milliseconds
    }
}


object LocalDateTypeAdapter : TypeAdapter<LocalDate>() {

    override fun write(out: JsonWriter, value: LocalDate?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.toString()) // yyyy-MM-dd
        }
    }

    override fun read(reader: JsonReader): LocalDate? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }

        return LocalDate.parse(reader.nextString())
    }
}

object HabitCompletionAdapter : TypeAdapter<HabitCompletion>() {

    override fun write(out: JsonWriter, value: HabitCompletion?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.ok)
        }
    }

    override fun read(reader: JsonReader): HabitCompletion? {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            return null
        }

        val num = reader.nextInt()
        return HabitCompletion.byValue(num)
    }
}