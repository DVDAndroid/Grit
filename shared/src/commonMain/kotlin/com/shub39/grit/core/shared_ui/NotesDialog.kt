package com.shub39.grit.core.shared_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.shub39.grit.core.habits.presentation.HabitDialogStatusInfo
import com.shub39.grit.core.habits.presentation.HabitsAction
import com.shub39.grit.core.habits.presentation.HabitsAction.CloseDialog
import com.shub39.grit.core.habits.presentation.StatusHabitAction
import com.shub39.grit.core.habits.presentation.StatusHabitDialogMode.Notes
import grit.shared.generated.resources.Res
import grit.shared.generated.resources.backspace
import org.jetbrains.compose.resources.vectorResource

@Composable
fun NotesDialog(
    state: HabitDialogStatusInfo,
    onAction: (HabitsAction) -> Unit,
) {
    var inputText by remember { mutableStateOf(state.notes) }
    AlertDialog(
        onDismissRequest = { onAction(CloseDialog(Notes)) },
        title = { Text("Enter day notes") },
        text = {
            Column {
                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Notes") },
                    singleLine = false,
                    trailingIcon = {
                        Icon(
                            imageVector = vectorResource(Res.drawable.backspace),
                            contentDescription = "clear text",
                            modifier = Modifier
                                .clickable {
                                    inputText = ""
                                }
                        )
                    }
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                onAction(
                    StatusHabitAction.SaveNoteDialog(
                        habit = state.habit,
                        date = state.date,
                        notes = inputText,
                    )
                )
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            Button(onClick = { onAction(CloseDialog(Notes)) }) {
                Text("Cancel")
            }
        }
    )
}