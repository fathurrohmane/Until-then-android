package com.elkusnandi.core.common

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

fun ZonedDateTime.getDuration(currentTime: ZonedDateTime = ZonedDateTime.now()): HashMap<TimeUnit, Number> {
    val duration = Duration.between(currentTime, this)

    return hashMapOf(
        TimeUnit.DAYS to duration.toDaysPart(),
        TimeUnit.HOURS to duration.toHoursPart(),
        TimeUnit.MINUTES to duration.toMinutesPart(),
        TimeUnit.SECONDS to duration.toSecondsPart(),
    )
}

fun epochToDateTime(epochSeconds: Long): String {
    val instant = Instant.ofEpochSecond(epochSeconds)
    val formatter = DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

fun stringDateTimeToEpoch(dateTime: String): Long {
    return try {
        ZonedDateTime.parse(dateTime).toEpochSecond()
    } catch (e: Exception) {
        -1
    }
}

fun String.addZeroPrefix(): String {
    return if (length == 1) {
        "0$this"
    } else {
        this
    }
}