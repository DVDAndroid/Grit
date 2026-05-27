package com.shub39.grit.core.shared_ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollField
import androidx.compose.material3.ScrollFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberScrollFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.shub39.grit.core.habits.presentation.HabitDialogStatusInfo
import com.shub39.grit.core.habits.presentation.HabitsAction
import com.shub39.grit.core.habits.presentation.HabitsAction.CloseDialog
import com.shub39.grit.core.habits.presentation.StatusHabitAction
import com.shub39.grit.core.habits.presentation.StatusHabitDialogMode.NumberValue
import grit.shared.generated.resources.Res
import grit.shared.generated.resources.delete
import grit.shared.generated.resources.delete_numbervalue
import grit.shared.generated.resources.keyboard
import grit.shared.generated.resources.swipe_vertical
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.math.round

private const val NUMBER_STEP = 0.1f
private const val NUMBER_TOTAL = 1001

@Composable
fun InputNumberDialog(
    state: HabitDialogStatusInfo,
    onAction: (HabitsAction) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var selectedNumber by remember { mutableStateOf(state.numberValue) }
    var inputText by remember { mutableStateOf(state.numberValue.toString()) }
    var scrollInputMode by remember { mutableStateOf(true) }

    val numbers = remember(selectedNumber) {
        FloatArray(NUMBER_TOTAL) { i ->
            val centerIndex = NUMBER_TOTAL / 2
            val offset = i - centerIndex
            round(((selectedNumber ?: 0f) + offset * NUMBER_STEP) * 10) / 10
        }
    }

    LaunchedEffect(Unit) {
        selectedNumber = state.numberValue
    }

    fun text2Number() {
        inputText.toFloatOrNull()?.let { selectedNumber = it }
    }
    LaunchedEffect(scrollInputMode) {
        if (scrollInputMode) {
            text2Number()
        } else {
            inputText = selectedNumber.toString()

            delay(150)
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    LaunchedEffect(selectedNumber) {
        inputText = selectedNumber.toString()
    }

    val scrollFieldState = rememberScrollFieldState(
        numbers.size,
        numbers.size / 2
    )

    LaunchedEffect(numbers) {
        scrollFieldState.scrollToOption(numbers.size / 2)
    }
    LaunchedEffect(scrollFieldState.selectedOption) {
        selectedNumber = numbers[scrollFieldState.selectedOption]
    }

    AlertDialog(
        onDismissRequest = { onAction(CloseDialog(NumberValue)) },
        title = { Text("Enter a value") },
        text = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    if (scrollInputMode) {
                        Box(
                            modifier = Modifier
                                .weight(0.8f)
                                .clip(MaterialTheme.shapes.medium)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.shapes.medium
                                )
                                .height(OutlinedTextFieldDefaults.MinHeight),
                            contentAlignment = Alignment.Center,
                        ) {
                            ScrollField(
                                state = scrollFieldState,
                                colors = ScrollFieldDefaults.colors(
                                    containerColor = OutlinedTextFieldDefaults.colors().unfocusedContainerColor,
                                ),
                                modifier = Modifier
                                    .fillMaxSize()
                            ) { idx, _ ->
                                Text(numbers[idx].toString())
                            }
                        }
                    } else {
                        OutlinedTextField(
                            shape = MaterialTheme.shapes.medium,
                            value = inputText,
                            onValueChange = { inputText = it },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Decimal
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                            ),
                            modifier = Modifier
                                .weight(0.8f)
                                .focusRequester(focusRequester)
                        )
                    }

                    IconButton(
                        onClick = {
                            scrollInputMode = !scrollInputMode
                        },
                        modifier = Modifier
                            .weight(0.2f),
                    ) {
                        Icon(
                            imageVector = vectorResource(
                                if (scrollInputMode)
                                    Res.drawable.keyboard
                                else
                                    Res.drawable.swipe_vertical
                            ),
                            contentDescription = null,
                        )
                    }
                }

                Button(
                    onClick = {
                        onAction(
                            StatusHabitAction.SaveNumberDialog(
                                habit = state.habit,
                                date = state.date,
                                numberValue = null,
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            imageVector = vectorResource(Res.drawable.delete),
                            contentDescription = null,
                        )

                        Text(
                            text = stringResource(Res.string.delete_numbervalue),
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    text2Number()

                    onAction(
                        StatusHabitAction.SaveNumberDialog(
                            habit = state.habit,
                            date = state.date,
                            numberValue = selectedNumber,
                        )
                    )
                },
                enabled = inputText.isNotBlank(),
            ) {
                Text("OK")
            }
        },

        dismissButton = {
            Button(onClick = { onAction(CloseDialog(NumberValue)) }) {
                Text("Cancel")
            }
        }
    )
}