package com.elkusnandi.untilthen.countdown.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.elkusnandi.core.common.toDefaultDateTimeFormat
import com.elkusnandi.core.design.components.DateTimePickerCountdownDialog
import com.elkusnandi.core.design.components.ReadOnlyTextField
import com.elkusnandi.core.design.components.rememberDateTimePickerState
import com.elkusnandi.core.design.theme.UntilThenTheme
import com.elkusnandi.untilthen.countdown.R
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetCountdown(
    modifier: Modifier = Modifier,
    onCreateCountdown: (String, Long) -> Unit,
    state: BottomSheetCountState
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()

    if (state.showBottomSheet) {
        val dateTimePickerState = rememberDateTimePickerState(state.dateTimeValue)
        var showDatePicker by remember { mutableStateOf(false) }

        ModalBottomSheet(
            onDismissRequest = {
                state.hideBottomSheet()
            },
            modifier = modifier,
            sheetState = sheetState
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = state.titleText ?: "",
                    onValueChange = { state.setTitle(it) },
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.date_time),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                val dateTimeValue = if (state.dateTimeValue != null)
                    state.dateTimeValue?.let {
                        ZonedDateTime.ofInstant(
                            Instant.ofEpochSecond(it / 1000),
                            ZoneId.systemDefault()
                        ).toDefaultDateTimeFormat()
                    } ?: ""
                else
                    stringResource(id = R.string.select_date_time)
                ReadOnlyTextField(dateTimeValue) {
                    showDatePicker = true
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            sheetState.hide()
                        }.invokeOnCompletion {
                            val title = state.titleText
                            if (title != null) {
                                onCreateCountdown(title, state.dateTimeValue!!)
                            }
                            state.hideBottomSheet()
                        }

                    }) {
                    Text(text = stringResource(id = if (state.isCreateNewData) R.string.create else R.string.update))
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        DateTimePickerCountdownDialog(
            showDatePicker = showDatePicker,
            state = dateTimePickerState,
            onDismiss = {
                showDatePicker = false
            }, onConfirm = {
                state.setDateTime(dateTimePickerState.getDateTimeEpochMillis())
                showDatePicker = false
            }
        )
    }
}

class BottomSheetCountState() {
    var titleText by mutableStateOf<String?>(null)
        private set

    var dateTimeValue by mutableStateOf<Long?>(null)
        private set

    var showBottomSheet by mutableStateOf(false)
        private set

    var isCreateNewData by mutableStateOf(true)
        private set

    fun setTitle(title: String) {
        titleText = title
    }

    fun setDateTime(dateTime: Long) {
        dateTimeValue = dateTime
    }

    fun expandBottomSheet(title: String? = null, selectedDateTimeValue: Long? = null) {
        titleText = title
        dateTimeValue = selectedDateTimeValue
        isCreateNewData = title == null && selectedDateTimeValue == null
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
        BottomSheetCountdown(Modifier, { _, _ -> }, state)
    }
}