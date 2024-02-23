package com.elkusnandi.untilthen.widget.ui.glance

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.glance.GlanceComposable
import androidx.glance.GlanceTheme
import androidx.glance.color.ColorProvider
import androidx.glance.material3.ColorProviders
import androidx.glance.text.TextStyle
import com.elkusnandi.core.design.theme.DarkColorScheme
import com.elkusnandi.core.design.theme.LightColorScheme

private object MyAppWidgetGlanceColorScheme {
    val colors = ColorProviders(
        light = LightColorScheme,
        dark = DarkColorScheme
    )
}

internal object UntilThenTextStyle {
    val default = TextStyle(color = ColorProvider(day = Color.Black, night = Color.White))
}

@Composable
internal fun UntilThenGlanceTheme(content: @GlanceComposable @Composable () -> Unit) {
    GlanceTheme(colors = MyAppWidgetGlanceColorScheme.colors) {
        content()
    }
}