package com.elkusnandi.core.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.elkusnandi.core.design.R
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DateTimePickerCountdownDialog(
    showDatePicker: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    state: DateTimePickerState
) {
    if (showDatePicker) {
        var datePickerState =
            rememberDatePickerState(initialSelectedDateMillis = state.dateEpochSeconds * 1000)
        var showTimePickers by remember { mutableStateOf(false) }
        DatePickerDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = { showTimePickers = true }
                ) {
                    Text(text = stringResource(id = R.string.select_time))
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
                state = datePickerState
            )
        }
        if (showTimePickers) {
            val timePickerState = rememberTimePickerState(
                initialHour = state.hour,
                initialMinute = state.minute
            )

            BasicAlertDialog(onDismissRequest = { showTimePickers = false }) {
                Surface(modifier = Modifier, shape = MaterialTheme.shapes.large) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(text = stringResource(id = R.string.select_time))
                        Spacer(modifier = Modifier.height(8.dp))
                        TimePicker(state = timePickerState)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = {
                                    showTimePickers = false
                                }
                            ) {
                                Text(text = stringResource(id = R.string.cancel))
                            }
                            TextButton(
                                onClick = {
                                    showTimePickers = false
                                    state.updateDateTime(
                                        datePickerState.selectedDateMillis!! / 1000,
                                        timePickerState.hour,
                                        timePickerState.minute,
                                    )
                                    onConfirm()
                                }
                            ) {
                                Text(text = stringResource(id = R.string.confirm))
                            }
                        }
                    }
                }
            }
        }
    }
}

class DateTimePickerState(
    initialDateEpochSecond: Long = ZonedDateTime.now().toEpochSecond(),
    initialHour: Int = 0,
    initialMinute: Int = 0
) {

    var dateEpochSeconds by mutableLongStateOf(initialDateEpochSecond)
        private set

    var hour by mutableIntStateOf(initialHour)
        private set

    var minute by mutableIntStateOf(initialMinute)
        private set

    fun updateDateTime(dateEpochSeconds: Long, newHour: Int, newMinute: Int) {
        this.dateEpochSeconds = dateEpochSeconds
        hour = newHour
        minute = newMinute
    }

    fun getDateTimeEpochMillis(): Long {
        var dateTime = LocalDateTime.ofInstant(
            Instant.ofEpochSecond(dateEpochSeconds),
            ZoneOffset.UTC
        )
        dateTime = dateTime.withHour(hour)
            .withMinute(minute)
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

}

@Composable
fun rememberDateTimePickerState(initialSelectedDateTimeMillis: Long?): DateTimePickerState {
    return remember {
        if (initialSelectedDateTimeMillis != null) {
            val instant = Instant.ofEpochMilli(initialSelectedDateTimeMillis)
            val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
            DateTimePickerState(
                localDateTime.toEpochSecond(ZoneOffset.UTC),
                localDateTime.hour,
                localDateTime.minute
            )
        } else {
            DateTimePickerState()
        }
    }
}