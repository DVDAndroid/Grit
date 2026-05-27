package com.shub39.grit.core.shared_ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import grit.shared.generated.resources.Res
import grit.shared.generated.resources.done
import org.jetbrains.compose.resources.stringResource

@Composable
fun GritDatePicker(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    state: DatePickerState = rememberDatePickerState(),
    onConfirm: () -> Unit,
) {
    DatePickerDialog(
        modifier = modifier.animateContentSize(animationSpec = tween(0)),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text(stringResource(Res.string.done)) }
        },
    ) {
        DatePicker(state = state)
    }
}