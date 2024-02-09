package com.elkusnandi.untilthen.countdown.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
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
    state: BottomSheetCountState
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

            LaunchedEffect(key1 = state.dateTimeValue) {
                val selectedDateMillis = state.dateTimeValue
                state.updateDateTime(selectedDateMillis)
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.titleText ?: "",
                    onValueChange = { state.updateTitle(it) },
                    label = { Text(text = stringResource(id = R.string.title)) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePicker = true
                        },
                    enabled = false,
                    value = if (state.dateTimeValue != null)
                        state.dateTimeValue?.let {
                            ZonedDateTime.ofInstant(
                                Instant.ofEpochSecond(it),
                                ZoneId.systemDefault()
                            ).toString()
                        } ?: ""
                    else
                        stringResource(id = R.string.select_date_time),
                    onValueChange = {
                        // not directly manipulated
                    },
                    label = { Text(text = stringResource(id = R.string.date_time)) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            val title = state.titleText
                            val dateTime = state.dateTimeValue
                            if (title != null && dateTime != null) {
                                onCreateCountdown(title, dateTime, countdown?.id)
                            }
                            state.hideBottomSheet()
                        }

                    }) {
                    Text(text = stringResource(id = if (countdown == null) R.string.create else R.string.update))
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        DatePickerCountdownDialog(
            showDatePicker = showDatePicker,
            state = datePickerState,
            onDismiss = {
                showDatePicker = false
            },
            onConfirm = {
                state.updateDateTime(datePickerState.selectedDateMillis?.div(1000))
                showDatePicker = false
            })
    }

}

class BottomSheetCountState() {
    var titleText by mutableStateOf<String?>(null)
        private set

    var dateTimeValue by mutableStateOf<Long?>(null)
        private set

    var showBottomSheet by mutableStateOf(false)
        private set

    fun updateTitle(title: String) {
        titleText = title
    }

    fun updateDateTime(dateTime: Long?) {
        dateTimeValue = dateTime
    }

    fun expandBottomSheet() {
        showBottomSheet = true
    }

    fun hideBottomSheet() {
        showBottomSheet = false
        titleText = null
        dateTimeValue = null
    }
}

@Composable
fun rememberBottomSheetCountState(
): BottomSheetCountState {
    return remember {
        BottomSheetCountState()
    }
}

@Preview
@Composable
private fun ButtonSheetCountdownPreview() {
    val state = rememberBottomSheetCountState()
    UntilThenTheme {
        BottomSheetCountdown(null, Modifier, { _, _, _ -> }, state)
    }
}