package com.shub39.grit.core.habits.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

class HabitRepoMock : HabitRepo {

    val habitList = mutableListOf<Habit>()
    val statusHabitList = hashMapOf<Long, MutableList<HabitStatus>>()

    override fun getHabitsWithAnalytics(): Flow<List<HabitWithAnalytics>> {
        TODO("Not yet implemented")
    }

    override fun getCompletedHabitIds(): Flow<List<Long>> {
        TODO("Not yet implemented")
    }

    override fun getOverallAnalytics(): Flow<OverallAnalytics> {
        TODO("Not yet implemented")
    }

    override suspend fun getCompletedHabitsForDate(date: LocalDate): List<Habit> {
        TODO("Not yet implemented")
    }

    override fun getHabitsWithStatus(): Flow<List<Pair<Habit, Boolean>>> {
        TODO("Not yet implemented")
    }

    override suspend fun upsertHabit(habit: Habit) {
        habitList.removeAll { it.id == habit.id }
        habitList += habit
    }

    override suspend fun deleteHabit(habitId: Long) {
        habitList.removeAll { it.id == habitId }
    }

    override suspend fun getHabits(): List<Habit> = habitList

    override suspend fun getHabitById(id: Long): Habit? = habitList.find { it.id == id }

    override suspend fun getHabitStatuses(): List<HabitStatus> = statusHabitList.values.flatten()

    override suspend fun getStatusForHabit(id: Long): List<HabitStatus> =
        statusHabitList[id].orEmpty().toList()

    override suspend fun getStatusByHabitAndDate(
        habitId: Long,
        date: LocalDate
    ): HabitStatus? = statusHabitList[habitId]?.find { it.date == date }

    override suspend fun upsertHabitStatus(habitStatus: HabitStatus) {
        val l = statusHabitList[habitStatus.habitId]
        if (l == null) statusHabitList[habitStatus.habitId] = mutableListOf()
        else {
            statusHabitList[habitStatus.habitId]?.removeAll { it.id == habitStatus.id }
        }
        statusHabitList[habitStatus.habitId]?.add(habitStatus)
    }

    override suspend fun deleteHabitStatus(habitId: Long, date: LocalDate) {
        val l = statusHabitList[habitId]
        if (l == null) statusHabitList[habitId] = mutableListOf()
        else {
            statusHabitList[habitId]?.removeAll { it.date == date }
        }
    }

}