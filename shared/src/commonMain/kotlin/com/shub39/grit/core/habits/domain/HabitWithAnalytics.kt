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
package com.shub39.grit.core.habits.domain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.shub39.grit.core.localized
import grit.shared.generated.resources.Res
import grit.shared.generated.resources.equal
import grit.shared.generated.resources.keyboard_arrow_down
import grit.shared.generated.resources.keyboard_arrow_up
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.char
import org.jetbrains.compose.resources.stringResource

typealias WeeklyComparisonData = List<Double>

typealias WeekDayFrequencyData = Map<DayOfWeek, Int>

/** Grouped model for [habit] and its constituent analytics calculated from [statuses] */
@Stable
@Immutable
data class HabitWithAnalytics(
    val habit: Habit,
    val consistency: Float,
    val statuses: List<HabitStatus>,
    val weeklyComparisonData: WeeklyComparisonData,
    val weekDayFrequencyData: WeekDayFrequencyData,
    val currentStreak: Int,
    val bestStreak: Int,
    val startedDaysAgo: Long,
    val numericAnalytics: HabitNumericAnalytics? = null,
)

@Stable
@Immutable
data class HabitNumericAnalytics(
//    val statusesByWeek: Map<AnalyticsPeriod.Week, List<HabitStatus>>,
//    val statusesByMonth: Map<AnalyticsPeriod.Month, List<HabitStatus>>,
//    val avgByWeek: Map<AnalyticsPeriod.Week, Float>,
//    val avgByMonth: Map<AnalyticsPeriod.Month, Float>,
//    val avgByYear: Map<AnalyticsPeriod.Year, Float>,
    val dailyChanges: List<HabitNumberChange>,
    val weeklyChanges: List<HabitNumberChange>,
    val monthlyChanges: List<HabitNumberChange>,
    val yearlyChanges: List<HabitNumberChange>,
)

@Stable
@Immutable
data class HabitNumberChange(
    val period: AnalyticsPeriod,
    val value: Float,
    val change: NumberChange,
) {
    enum class NumberChange {
        Increase,
        Decrease,
        Equal;

        fun icon() = when (this) {
            Increase -> Res.drawable.keyboard_arrow_up
            Decrease -> Res.drawable.keyboard_arrow_down
            Equal -> Res.drawable.equal
        }
    }
}

sealed class AnalyticsPeriod : Comparable<AnalyticsPeriod> {

    @Composable
    abstract fun toComposeString(): String

    data class Day(val date: LocalDate) : AnalyticsPeriod() {

        @Composable
        override fun toComposeString() = buildString {
            append(YearMonth(date.year, date.month).format(
                YearMonth.Format {
                    year()
                    char(' ')
                    monthName(MonthNames.ENGLISH_FULL)
                }
            ))
            append(" ")
            append(date.day.toString().padStart(2, '0'))
            append(" ")
            append(stringResource(date.dayOfWeek.localized()))
        }

        override fun compareTo(other: AnalyticsPeriod): Int {
            if (other !is Day) throw IllegalArgumentException()
            return compareValuesBy(
                this,
                other,
                { it.date.year },
                { it.date.month },
                { it.date.day })
        }
    }

    data class Week(val year: Int, val week: Int) : AnalyticsPeriod() {
        @Composable
        override fun toComposeString(): String = "$year week $week"

        override fun compareTo(other: AnalyticsPeriod): Int {
            if (other !is Week) throw IllegalArgumentException()
            return compareValuesBy(this, other, { it.year }, { it.week })
        }
    }

    data class Month(val year: Int, val month: kotlinx.datetime.Month) : AnalyticsPeriod() {
        @Composable
        override fun toComposeString(): String = YearMonth(year, month).format(
            YearMonth.Format {
                year()
                char(' ')
                monthName(MonthNames.ENGLISH_FULL)
            }
        )

        override fun compareTo(other: AnalyticsPeriod): Int {
            if (other !is Month) throw IllegalArgumentException()
            return compareValuesBy(this, other, { it.year }, { it.month })
        }
    }

    data class Year(val year: Int) : AnalyticsPeriod() {
        @Composable
        override fun toComposeString(): String = year.toString()

        override fun compareTo(other: AnalyticsPeriod): Int {
            if (other !is Year) throw IllegalArgumentException()
            return compareValuesBy(this, other, { it.year })
        }
    }
}