package com.elkusnandi.untilthen.widget

import android.content.Context
import androidx.glance.appwidget.updateAll
import com.elkusnandi.untilthen.widget.ui.glance.MyAppWidget

internal suspend fun Context.updateAllGlance() {
    MyAppWidget().updateAll(this)
}