package com.elkusnandi.data.countdown.model

import com.elkusnandi.data.countdown.source.local.model.CountdownEntity
import java.time.ZonedDateTime

data class Countdown(
    val id: Long,
    val title: String,
    val dateTime: ZonedDateTime,
    val color: String
)

internal fun Countdown.toEntity() = CountdownEntity(
    id, title, dateTime, color
)