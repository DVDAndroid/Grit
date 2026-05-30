package com.shub39.grit.core.habits.domain

import com.shub39.grit.core.habits.presentation.StatusHabitAction
import com.shub39.grit.core.now
import com.shub39.grit.habits.data.repository.upsertLogic
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import org.junit.Before
import org.junit.Test

class UpsertStatusLogicTest {

    private lateinit var repo: HabitRepoMock
    private lateinit var time: LocalDateTime
    private lateinit var habit: Habit

    @Before
    fun setup() {
        repo = HabitRepoMock()
        time = LocalDateTime.now()
        habit = Habit(
            id = 1,
            title = "title",
            description = "description",
            time = time,
            days = setOf(DayOfWeek.MONDAY),
            index = 0,
            reminder = false,
            type = HabitType.Boolean
        )
    }

    // 1.click diretto sull'habit, nuovo, tipo bool
    @Test
    fun directInsert_bool_new() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.ToggleBooleanStatus(
                habit = habit,
                date = time.date,
            )
        )

        val s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == null)
        assert(s.numberValue == null)
    }

    // 1.click diretto sull'habit, nuovo, tipo bool
    // 2.click diretto su habit per rimuoverlo
    @Test
    fun directInsert_bool_old_0notes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.ToggleBooleanStatus(
                habit = habit,
                date = time.date,
            )
        )

        var s = status()
        assert(s != null)

        repo.upsertLogic(
            StatusHabitAction.ToggleBooleanStatus(
                habit = habit,
                date = time.date,
            )
        )

        s = status()
        assert(s == null)
    }

    // 1.click su habit diretto, tipo bool
    // 2.aggiunta note
    @Test
    fun directInsert_bool_old_someNotes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.ToggleBooleanStatus(
                habit = habit,
                date = time.date,
            )
        )

        var s = status()
        assert(s != null)
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note"
            )
        )

        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == "note")
        assert(s.numberValue == null)
    }

    // 1.long click su habit solo per aggiungere note, tipo bool
    @Test
    fun longPress_bool_new_someNotes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note"
            )
        )

        val s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)
    }

    // 1.long click su habit solo per aggiungere note, ma vuote, quindi nessuna insert, tipo bool
    @Test
    fun longPress_bool_new_emptyNotes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = ""
            )
        )

        val s = status()
        assert(s == null)
    }

    // 1.long click su habit solo per aggiungere note, tipo bool
    // 2.note "", quindi cancellazione
    @Test
    fun longPress_bool_new_deleteNotes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note"
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)

        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = ""
            )
        )

        s = status()
        assert(s == null)
    }

    // 1.click su habit diretto, tipo bool
    // 2.aggiunta note
    // 3.note "", quindi cancellazione
    @Test
    fun directInsert_bool_old_deleteNotes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.ToggleBooleanStatus(
                habit = habit,
                date = time.date,
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == null)
        assert(s.numberValue == null)
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note"
            )
        )

        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == "note")
        assert(s.numberValue == null)

        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = ""
            )
        )

        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == null)
        assert(s.numberValue == null)
    }

    // 1.long click su habit solo per aggiungere note, tipo bool
    // 2.click su habit diretto
    @Test
    fun longPress_bool_new_notes_directInsert() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note"
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)

        repo.upsertLogic(
            StatusHabitAction.ToggleBooleanStatus(
                habit = habit,
                date = time.date,
            )
        )
        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == "note")
        assert(s.numberValue == null)
    }

    // 1.long click su habit solo per aggiungere note, tipo bool
    // 2.click su habit diretto
    // 3.click per togliere completed, lasciare note
    @Test
    fun longPress_bool_new_notes_directInsert2() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note"
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)

        repo.upsertLogic(
            StatusHabitAction.ToggleBooleanStatus(
                habit = habit,
                date = time.date,
            )
        )
        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == "note")
        assert(s.numberValue == null)

        repo.upsertLogic(
            StatusHabitAction.ToggleBooleanStatus(
                habit = habit,
                date = time.date,
            )
        )
        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)
    }

    ///


    // 1.click diretto sull'habit, nuovo, tipo numeric
    @Test
    fun directInsert_number_new() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = 1f,
            )
        )

        val s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == null)
        assert(s.numberValue == 1f)
    }

    // 1.click diretto sull'habit, nuovo, tipo numeric
    // 2.click diretto su habit per rimuoverlo
    @Test
    fun directInsert_number_old_0notes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = 1f,
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.numberValue == 1f)

        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = null,
            )
        )

        s = status()
        assert(s == null)
    }

    // 1.click su habit diretto, tipo numeric
    // 2.aggiunta note
    @Test
    fun directInsert_number_old_someNotes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = 1f,
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.numberValue == 1f)

        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note",
            )
        )

        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == "note")
        assert(s.numberValue == 1f)
    }

    // 1.long click su habit solo per aggiungere note, tipo numeric
    @Test
    fun longPress_number_new_someNotes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note",
            )
        )

        val s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)
    }

    // 1.long click su habit solo per aggiungere note, tipo numeric
    // 2.note "", quindi cancellazione
    @Test
    fun longPress_number_new_deleteNotes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note",
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)

        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "",
            )
        )

        s = status()
        assert(s == null)
    }

    // 1.click su habit diretto, tipo numeric
    // 2.aggiunta note
    // 3.note "", quindi cancellazione
    @Test
    fun directInsert_number_old_deleteNotes() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = 1f,
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == null)
        assert(s.numberValue == 1f)
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note",
            )
        )

        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == "note")
        assert(s.numberValue == 1f)

        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "",
            )
        )

        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == null)
        assert(s.numberValue == 1f)
    }

    // 1.long click su habit solo per aggiungere note, tipo numeric
    // 2.click su habit diretto
    @Test
    fun longPress_number_new_notes_directInsert() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note",
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)

        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = 1f,
            )
        )
        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == "note")
        assert(s.numberValue == 1f)
    }

    // 1.long click su habit solo per aggiungere note, tipo numeric
    // 2.click su habit diretto
    // 3.click per togliere completed, lasciare note
    @Test
    fun longPress_number_new_notes_directInsert2() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note",
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)

        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = 1f,
            )
        )
        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == "note")
        assert(s.numberValue == 1f)

        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = null,
            )
        )
        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)
    }

    // 1.long click su habit solo per aggiungere note, tipo numeric
    // 2.click su habit diretto
    // 3.togliere note
    @Test
    fun longPress_number_new_notes_directInsert2_remove() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "note",
            )
        )

        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.OnlyNotes)
        assert(s.notes == "note")
        assert(s.numberValue == null)

        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = 1f,
            )
        )
        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == "note")
        assert(s.numberValue == 1f)

        repo.upsertLogic(
            StatusHabitAction.SaveNoteDialog(
                habit = habit,
                date = time.date,
                notes = "",
            )
        )
        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == null)
        assert(s.numberValue == 1f)
    }

    // 1.click su habit diretto per inserire numero
    // 2.click su habit diretto per aggiornare numero
    @Test
    fun directInsert_number_directInsert() = runBlocking {
        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = 1f,
            )
        )
        var s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == null)
        assert(s.numberValue == 1f)

        repo.upsertLogic(
            StatusHabitAction.SaveNumberDialog(
                habit = habit,
                date = time.date,
                numberValue = 5f,
            )
        )
        s = status()
        assert(s != null)
        requireNotNull(s)
        assert(s.ok == HabitCompletion.Completed)
        assert(s.notes == null)
        assert(s.numberValue == 5f)
    }


    private suspend fun status(): HabitStatus? = repo.getStatusByHabitAndDate(habit.id, time.date)

}