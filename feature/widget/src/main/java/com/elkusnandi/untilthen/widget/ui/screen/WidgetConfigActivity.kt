package com.elkusnandi.untilthen.widget.ui.screen

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.elkusnandi.core.common.getDuration
import com.elkusnandi.core.design.components.CountDownItem
import com.elkusnandi.core.design.theme.UntilThenTheme
import com.elkusnandi.data.countdown.model.Countdown
import com.elkusnandi.untilthen.widget.R
import com.elkusnandi.untilthen.widget.data.WidgetConfig
import com.elkusnandi.untilthen.widget.data.WidgetDataStateDefinition
import com.elkusnandi.untilthen.widget.ui.glance.MyAppWidget
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.time.ZonedDateTime

@AndroidEntryPoint
class WidgetConfigActivity : ComponentActivity() {

    private val viewModel by viewModels<WidgetConfigViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        val appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        val config = getClockConfigByWidgetId(appWidgetId)

        setContent {
            UntilThenTheme {
                val countdowns = viewModel.countdowns.collectAsLazyPagingItems()

                WidgetConfigScreen(appWidgetId, config, countdowns) {
                    val resultValue = Intent()
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    setResult(RESULT_OK, resultValue)
                    finish()
                }
            }
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WidgetConfigScreen(
    appWidgetId: Int,
    widgetConfig: WidgetConfig,
    countdowns: LazyPagingItems<Countdown>,
    onFinish: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.select_countdown)) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) {

        CountdownLazyColumn(
            countdowns = countdowns,
            modifier = Modifier.padding(it),
            onClickItem = { item ->
                saveConfig(
                    appWidgetId,
                    context,
                    WidgetConfig(item.title, item.dateTime)
                ) {
                    onFinish()
                }
            }
        )
    }

}

@Composable
private fun CountdownLazyColumn(
    countdowns: LazyPagingItems<Countdown>,
    modifier: Modifier = Modifier,
    onClickItem: (Countdown) -> Unit = {},
    onLongClickItem: (Countdown) -> Unit = {}
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
                duration = countdown.dateTime.getDuration(currentTime = currentTime),
                onClick = { onClickItem(countdown) },
                onLongClick = { onLongClickItem(countdown) }
            )
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

fun Context.getClockConfigByWidgetId(appWidgetId: Int): WidgetConfig = runBlocking {
    val manager = GlanceAppWidgetManager(this@getClockConfigByWidgetId)

    val glanceIds = manager.getGlanceIds(MyAppWidget::class.java)
    var config: WidgetConfig? = null
    glanceIds.forEach {
        if (manager.getAppWidgetId(it) == appWidgetId) {
            config =
                getAppWidgetState(this@getClockConfigByWidgetId, WidgetDataStateDefinition, it)
        }
    }
    config ?: WidgetConfig()
}

fun saveConfig(
    appWidgetId: Int,
    context: Context,
    widgetConfig: WidgetConfig,
    onFinish: () -> Unit
) {
    val manager = GlanceAppWidgetManager(context)

    runBlocking {
        val glanceIds = manager.getGlanceIds(MyAppWidget::class.java)

        glanceIds.forEach {
            if (manager.getAppWidgetId(it) == appWidgetId) {
                updateAppWidgetState(
                    context = context,
                    glanceId = it,
                    definition = WidgetDataStateDefinition,
                    updateState = { widgetConfig }
                )
                MyAppWidget().update(context, it)
            }
        }
    }

    onFinish()
}