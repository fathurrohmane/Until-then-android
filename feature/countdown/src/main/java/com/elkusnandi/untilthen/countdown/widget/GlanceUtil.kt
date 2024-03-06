package com.elkusnandi.untilthen.countdown.widget

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.state.getAppWidgetState
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import com.elkusnandi.untilthen.countdown.widget.data.WidgetConfig
import com.elkusnandi.untilthen.countdown.widget.data.WidgetDataStateDefinition
import com.elkusnandi.untilthen.countdown.widget.ui.glance.MyAppWidget
import kotlinx.coroutines.runBlocking

internal suspend fun Context.updateAllGlance() {
    MyAppWidget().updateAll(this)
}

internal fun Context.getClockConfigByWidgetId(appWidgetId: Int): WidgetConfig = runBlocking {
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

internal fun saveConfig(
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