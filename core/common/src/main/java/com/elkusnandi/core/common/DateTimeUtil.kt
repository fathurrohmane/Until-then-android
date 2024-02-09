package com.elkusnandi.core.common

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun ZonedDateTime.getDuration(currentTime: ZonedDateTime = ZonedDateTime.now()): HashMap<TimeUnit, Number> {
    val duration = Duration.between(currentTime, this)

    val days = duration.toDaysPart()
    val hours = duration.toHoursPart()
    val minutes = duration.toMinutesPart()
    val seconds = duration.toSecondsPart()

    return hashMapOf(
        TimeUnit.DAYS to days,
        TimeUnit.HOURS to if (days == 0L) hours else abs(hours),
        TimeUnit.MINUTES to if (days == 0L && hours == 0) minutes else abs(minutes),
        TimeUnit.SECONDS to if (days == 0L && hours == 0 && minutes == 0) seconds else abs(seconds)
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
        if (length == 2) {
            if (this[0] == '-') {
                "-0${this[1]}"
            } else {
                this
            }
        } else {
            this
        }
    }
}
// -1 -10 -20