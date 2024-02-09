package com.elkusnandi.untilthen.countdown.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.elkusnandi.core.design.theme.UntilThenTheme
import com.elkusnandi.data.countdown.model.Countdown
import com.elkusnandi.untilthen.countdown.R
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCountdown(
    countdown: Countdown?,
    modifier: Modifier = Modifier,
    onCreateCountdown: (String, Long, Long?) -> Unit,
    state: BottomSheetCountState = rememberBottomSheetCountState(
        countdown?.title,
        countdown?.dateTime.toString()
    )
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    if (state.showBottomSheet) {
        LaunchedEffect(key1 = countdown) {
            state.updateDateTime(countdown?.dateTime?.toEpochSecond())
            countdown?.title?.let { state.updateTitle(it) }
        }

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = countdown?.dateTime?.toEpochSecond()?.times(1000)
        )
        var showDatePicker by remember { mutableStateOf(false) }

        ModalBottomSheet(
            onDismissRequest = {
                state.hideBottomSheet()
            },
            modifier = modifier,
            sheetState = sheetState
        ) {

            LaunchedEffect(key1 = state.dateTimeText) {
                val selectedDateMillis = state.dateTimeText
                state.updateDateTime(selectedDateMillis)
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.titleText ?: "",
                    onValueChange = { state.updateTitle(it) },
                    label = { Text("Title") }
                )

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePicker = true
                        },
                    enabled = false,
                    value = if (state.dateTimeText != null) ZonedDateTime.ofInstant(
                        Instant.ofEpochSecond(state.dateTimeText!!),
                        ZoneId.systemDefault()
                    ).toString() else
                        "(Select date time)",
                    onValueChange = { },
                    label = { Text("Date time") }
                )

                Button(onClick = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        val title = state.titleText
                        val dateTime = state.dateTimeText
                        if (title != null && dateTime != null) {
                            onCreateCountdown(title, dateTime, countdown?.id)
                        }
                        state.hideBottomSheet()
                    }

                }) {
                    Text(text = stringResource(id = if (countdown == null) R.string.create else R.string.update))
                }
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = {
                    showDatePicker = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            state.updateDateTime(datePickerState.selectedDateMillis?.div(1000))
                            showDatePicker = false
                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }
    }

}

class BottomSheetCountState(title: String? = null, dateTime: String? = null) {
    var titleText by mutableStateOf(title)
        private set

    var dateTimeText by mutableStateOf(
        if (dateTime == null) null else ZonedDateTime.parse(dateTime).toEpochSecond()
    )
        private set

    var showBottomSheet by mutableStateOf(false)
        private set

    fun updateTitle(title: String) {
        titleText = title
    }

    fun updateDateTime(dateTime: Long?) {
        dateTimeText = dateTime
    }

    fun expandBottomSheet(title: String? = null, dateTime: Long? = null) {
        titleText = title
        dateTimeText = dateTime
        showBottomSheet = true
    }

    fun hideBottomSheet() {
        showBottomSheet = false
        titleText = null
        dateTimeText = null
    }
}

@Composable
fun rememberBottomSheetCountState(
    title: String? = null,
    dateTime: String? = null
): BottomSheetCountState {
    return remember {
        BottomSheetCountState(title, dateTime)
    }
}

@Preview
@Composable
private fun ButtonSheetCountdownPreview() {
    UntilThenTheme {
        BottomSheetCountdown(null, Modifier, { _, _, _ -> })
    }
}