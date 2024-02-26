package com.elkusnandi.untilthen.countdown.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.elkusnandi.core.common.getDuration
import com.elkusnandi.core.common.toEpochMillis
import com.elkusnandi.core.design.components.BasicInfoView
import com.elkusnandi.core.design.components.BasicItemInfoView
import com.elkusnandi.core.design.components.ConfirmDialog
import com.elkusnandi.core.design.components.CountDownItem
import com.elkusnandi.core.design.theme.UntilThenTheme
import com.elkusnandi.data.countdown.model.Countdown
import com.elkusnandi.untilthen.countdown.R
import com.elkusnandi.untilthen.countdown.ui.component.BottomSheetCountdown
import com.elkusnandi.untilthen.countdown.ui.component.rememberBottomSheetCountState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime

@AndroidEntryPoint
class CountdownScreenFragment : Fragment() {

    private val viewModel: CountdownViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireActivity()).apply {
        setContent {
            UntilThenTheme {
                val countdowns = viewModel.countdowns.collectAsLazyPagingItems()
                CountdownScreen(
                    countdowns = countdowns,
                    onCreateCountdown = { title, dateTime, countDownId ->
                        viewModel.addCountdown(
                            Countdown(
                                countDownId ?: 0L,
                                title,
                                ZonedDateTime.ofInstant(
                                    Instant.ofEpochSecond(dateTime / 1000),
                                    ZoneOffset.UTC
                                ),
                                ""
                            )
                        )
                    },
                    onDeleteCountdown = {
                        viewModel.deleteCountdown(it)
                    })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountdownScreen(
    countdowns: LazyPagingItems<Countdown>,
    onCreateCountdown: (String, Long, Long?) -> Unit,
    onDeleteCountdown: (Countdown) -> Unit,
) {
    val bottomSheetState = rememberBottomSheetCountState()
    var selectedCountdown by remember { mutableStateOf<Countdown?>(null) }
    var showConfirmDialog by remember { mutableStateOf<Countdown?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.countdown)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = stringResource(id = R.string.create)) },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = {
                    selectedCountdown = null
                    bottomSheetState.expandBottomSheet()
                }
            )
        }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (countdowns.loadState.refresh) {
                is LoadState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(64.dp)
                        )
                    }
                }

                is LoadState.Error -> {
                    val loadState = countdowns.loadState.refresh as LoadState.Error
                    BasicInfoView(
                        text = stringResource(id = R.string.something_went_wrong),
                        detail = loadState.error.message.toString()
                    ) {
                        Button(onClick = { countdowns.refresh() }) {
                            Text(text = stringResource(id = R.string.retry))
                        }
                    }

                }

                is LoadState.NotLoading -> {
                    CountdownLazyColumn(
                        countdowns = countdowns,
                        onClickItem = {
                            selectedCountdown = it
                            bottomSheetState.expandBottomSheet(
                                it.title,
                                it.dateTime.toEpochMillis()
                            )
                        },
                        onLongClickItem = {
                            showConfirmDialog = it
                        })
                }
            }
        }

        BottomSheetCountdown(
            state = bottomSheetState,
            onCreateCountdown = { title, dateTime ->
                onCreateCountdown(
                    title,
                    dateTime,
                    selectedCountdown?.id
                )
            }
        )
    }

    if (showConfirmDialog != null) {
        ConfirmDialog(
            dialogTitle = stringResource(id = R.string.delete_title),
            dialogText = stringResource(id = R.string.delete_body),
            onDismissRequest = {
                showConfirmDialog = null
            },
            onConfirmation = {
                onDeleteCountdown(showConfirmDialog!!)
                showConfirmDialog = null
            })
    }

}

@Composable
private fun CountdownLazyColumn(
    countdowns: LazyPagingItems<Countdown>,
    modifier: Modifier = Modifier,
    onClickItem: (Countdown) -> Unit = {},
    onLongClickItem: (Countdown) -> Unit = {}
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    var currentTime by remember { mutableStateOf(ZonedDateTime.now()) }
    var isCountdownRunning by remember { mutableStateOf(true) }

    // Update timer every second
    LaunchedEffect(key1 = currentTime, key2 = isCountdownRunning) {
        if (isCountdownRunning) {
            currentTime = ZonedDateTime.now()
            delay(1000)
        }
    }

    // Only start countdown when app in foreground
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                isCountdownRunning = true
            } else if (event == Lifecycle.Event.ON_STOP) {
                isCountdownRunning = false
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    if (countdowns.loadState.refresh == LoadState.NotLoading(false) && countdowns.itemCount == 0) {
        BasicInfoView(
            text = stringResource(id = R.string.no_item),
            detail = stringResource(id = R.string.no_item_detail)
        )
    } else {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.fillMaxSize()
        ) {
            // content
            items(countdowns.itemCount, key = countdowns.itemKey { it.id }) {
                val countdown = countdowns[it] ?: return@items

                CountDownItem(
                    title = countdown.title,
                    duration = countdown.dateTime.getDuration(currentTime = currentTime),
                    onClick = { onClickItem(countdown) },
                    onLongClick = { onLongClickItem(countdown) }
                )
            }

            // Append loading
            if (countdowns.loadState.append == LoadState.Loading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            } else if (countdowns.loadState.append is LoadState.Error) { // Append error
                val errorMessage =
                    (countdowns.loadState.append as LoadState.Error).error.message.toString()
                item {
                    BasicItemInfoView(
                        text = errorMessage,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        TextButton(onClick = {
                            countdowns.retry()
                        }) {
                            Text(text = stringResource(id = R.string.retry))
                        }
                    }
                }
            }
        }
    }

}

@Preview
@Composable
private fun CountdownScreenPreview() {
    val fakeCountdown =
        flowOf(
            PagingData.from(
                listOf(Countdown(0L, "Interview", ZonedDateTime.now().plusDays(5), "")),
                sourceLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                ),
                mediatorLoadStates = LoadStates(
                    refresh = LoadState.NotLoading(false),
                    append = LoadState.NotLoading(false),
                    prepend = LoadState.NotLoading(false),
                ),
            )
        ).collectAsLazyPagingItems()

    MaterialTheme { // using theme from another module doesn't work
        CountdownScreen(fakeCountdown, { _, _, _ -> }, {})
    }
}