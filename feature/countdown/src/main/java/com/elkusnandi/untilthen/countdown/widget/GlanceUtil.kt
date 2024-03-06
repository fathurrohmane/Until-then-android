package com.elkusnandi.untilthen.countdown.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.elkusnandi.untilthen.countdown.widget.ui.glance.MyAppWidget

internal suspend fun Context.updateAllGlance() {
    MyAppWidget().updateAll(this)
}