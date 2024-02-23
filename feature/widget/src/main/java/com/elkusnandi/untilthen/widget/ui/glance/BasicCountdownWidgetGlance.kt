package com.elkusnandi.untilthen.widget.ui.glance

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.background
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.elkusnandi.untilthen.widget.R
import com.elkusnandi.untilthen.widget.data.WidgetConfig
import com.elkusnandi.untilthen.widget.data.WidgetDataStateDefinition
import java.time.Duration
import java.time.ZonedDateTime

class MyAppWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MyAppWidget()
}

class MyAppWidget : GlanceAppWidget() {

    override val stateDefinition = WidgetDataStateDefinition
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val clockConfig = currentState<WidgetConfig>()

            GlanceTheme {
                ClockFaceGlance(clockConfig)
            }
        }
    }
}

@Composable
fun ClockFaceGlance(config: WidgetConfig, modifier: GlanceModifier = GlanceModifier) {

    GlanceTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(day = Color.White, night = Color.Black)
                .cornerRadius(16.dp)
                .appWidgetBackground(),
            verticalAlignment = Alignment.Vertical.CenterVertically,
        ) {
            Text(text = config.title)
            Row {
                Text(
                    text = Duration.between(ZonedDateTime.now(), config.dateTime).toDaysPart()
                        .toString(),
                    style = TextStyle(fontSize = 42.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = GlanceModifier.width(8.dp))
                Text(text = LocalContext.current.getString(R.string.days))
            }
        }
    }
}
