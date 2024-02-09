package com.elkusnandi.untilthen.countdown.ui.component

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.elkusnandi.untilthen.countdown.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DatePickerCountdownDialog(
    showDatePicker: Boolean,
    state: DatePickerState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text(text = stringResource(id = R.string.test))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onDismiss
                ) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = state
            )
        }
    }
}