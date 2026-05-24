package com.shub39.grit.core.habits.domain

import com.shub39.grit.core.utils.now
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import org.junit.Test

class HabitRepoTest {
    @Test
    fun repoTest() {
        val repo = HabitRepoMock()
        val time = LocalDateTime.now()
        runBlocking {
            val habit = Habit(
                id = 1,
                title = "title",
                description = "description",
                time = time,
                days = setOf(DayOfWeek.MONDAY),
                index = 0,
                reminder = false,
                type = HabitType.Boolean
            )
            repo.upsertHabit(habit)

            assert(repo.habitList.size == 1)

            val justInserted = repo.getHabitById(1)
            assert(habit == justInserted)

            assert(repo.getStatusForHabit(1).isEmpty())

            assert(repo.getHabitById(1)?.index == 0)
            repo.upsertHabit(habit.copy(index = 1))
            assert(repo.getHabitById(1)?.index == 1)

            val habitStatus = HabitStatus(
                id = 1,
                habitId = 1,
                date = time.date,
                ok = HabitCompletion.Completed,
                notes = null,
                numberValue = null,
            )
            repo.insertHabitStatus(habitStatus)
            assert(repo.getStatusForHabit(1).singleOrNull() == habitStatus)

            assert(repo.getStatusForHabit(1).singleOrNull()?.notes == null)
            repo.upsertHabitStatus(habitStatus.copy(notes = "a"))
            assert(repo.getStatusForHabit(1).singleOrNull()?.notes == "a")

            repo.deleteHabit(1)
            assert(repo.getHabits().isEmpty())
        }
    }
}