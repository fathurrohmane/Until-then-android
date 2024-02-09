package com.elkusnandi.core.common

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class DateTimeUtilKtTest {

    @Test
    fun getDuration_durationOneSecondLater_returnOneSecond() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusSeconds(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(0, duration[TimeUnit.MINUTES])
        assertEquals(0, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

    @Test
    fun getDuration_durationOneMinuteLater_returnOneMinute() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusSeconds(1).plusMinutes(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(1, duration[TimeUnit.MINUTES])
        assertEquals(0, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

    @Test
    fun getDuration_durationOneHourLater_returnOneHour() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusSeconds(1).plusMinutes(1).plusHours(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(1, duration[TimeUnit.MINUTES])
        assertEquals(1, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

    @Test
    fun getDuration_durationOneDayLater_returnOneDay() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusSeconds(1).plusMinutes(1).plusHours(1).plusDays(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(1, duration[TimeUnit.MINUTES])
        assertEquals(1, duration[TimeUnit.HOURS])
        assertEquals(1L, duration[TimeUnit.DAYS])
    }

    @Test
    fun getDuration_durationOneDayLaterMinusOneSecond_returnZeroDay23Hours59Minute59Second() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusDays(1).minusSeconds(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(59, duration[TimeUnit.SECONDS])
        assertEquals(59, duration[TimeUnit.MINUTES])
        assertEquals(23, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }
}