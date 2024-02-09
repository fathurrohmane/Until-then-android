package com.elkusnandi.untilthen.countdown.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.elkusnandi.core.common.getDuration
import com.elkusnandi.core.design.components.CountDownItem
import com.elkusnandi.core.design.theme.UntilThenTheme
import com.elkusnandi.data.countdown.model.Countdown
import com.elkusnandi.untilthen.countdown.ui.component.BottomSheetCountdown
import com.elkusnandi.untilthen.countdown.ui.component.rememberBottomSheetCountState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import java.time.Instant
import java.time.ZoneId
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
                CountdownScreen(countdowns) { title, dateTime, countDownId ->
                    viewModel.addCountdown(
                        Countdown(
                            countDownId ?: 0L,
                            title,
                            ZonedDateTime.ofInstant(
                                Instant.ofEpochSecond(dateTime),
                                ZoneId.systemDefault()
                            ),
                            "red"
                        )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountdownScreen(
    countdowns: LazyPagingItems<Countdown>,
    onCreateCountdown: (String, Long, Long?) -> Unit
) {
    val bottomSheetState = rememberBottomSheetCountState()
    var selectedCountdown by remember { mutableStateOf<Countdown?>(null) }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Until then") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Create") },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                onClick = {
                    bottomSheetState.expandBottomSheet()
                }
            )
        }) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            when (countdowns.loadState.refresh) {
                is LoadState.Loading -> {
                    // todo
                }

                is LoadState.Error -> {
                    // todo
                }

                is LoadState.NotLoading -> {
                    UserLazyColumn(countdowns, {
                        selectedCountdown = it
                        bottomSheetState.expandBottomSheet()
                    })
                }
            }
        }

        BottomSheetCountdown(
            selectedCountdown,
            state = bottomSheetState,
            onCreateCountdown = onCreateCountdown
        )
    }
}

@Composable
private fun UserLazyColumn(
    countdowns: LazyPagingItems<Countdown>,
    onClickItem: (Countdown) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    var currentTime by remember { mutableStateOf(ZonedDateTime.now()) }
    var isRunning by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = currentTime, key2 = isRunning) {
        if (isRunning) {
            delay(1000)
            currentTime = ZonedDateTime.now()
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                isRunning = true
            } else if (event == Lifecycle.Event.ON_STOP) {
                isRunning = false
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {

        items(countdowns.itemCount, key = countdowns.itemKey { it.id }) {
            val countdown = countdowns[it] ?: return@items

            CountDownItem(
                title = countdown.title,
                duration = countdown.dateTime.getDuration(currentTime = currentTime)
            ) {
                onClickItem(countdown)
            }
        }

        if (countdowns.loadState.append == LoadState.Loading) {
            item {
                CircularProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }
        } else if (countdowns.loadState.append is LoadState.Error) {
            val errorMessage =
                (countdowns.loadState.append as LoadState.Error).error.message.toString()
            item {
//                UserPagingError(errorMessage) {
//                    users.retry()
//                }
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
                listOf<Countdown>()
            )
        ).collectAsLazyPagingItems()

    UntilThenTheme {
        CountdownScreen(fakeCountdown) { _, _, _ -> }
    }
}