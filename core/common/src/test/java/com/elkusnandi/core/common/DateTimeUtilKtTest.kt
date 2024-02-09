package com.elkusnandi.core.common

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class DateTimeUtilKtTest {

    @Test
    fun `getDuration duration 1 second return 1 second`() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusSeconds(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(0, duration[TimeUnit.MINUTES])
        assertEquals(0, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

    @Test
    fun `getDuration duration 1 minute return 1 minute`() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusSeconds(1).plusMinutes(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(1, duration[TimeUnit.MINUTES])
        assertEquals(0, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

    @Test
    fun `getDuration duration 1 hour return 1 hour`() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusSeconds(1).plusMinutes(1).plusHours(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(1, duration[TimeUnit.MINUTES])
        assertEquals(1, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

    @Test
    fun `getDuration duration 1 day return 1 day`() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusSeconds(1).plusMinutes(1).plusHours(1).plusDays(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(1, duration[TimeUnit.MINUTES])
        assertEquals(1, duration[TimeUnit.HOURS])
        assertEquals(1L, duration[TimeUnit.DAYS])
    }

    @Test
    fun `getDuration duration 1 day -1 second return 0 day 23 hour 59 minute 59 second`() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusDays(1).minusSeconds(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(59, duration[TimeUnit.SECONDS])
        assertEquals(59, duration[TimeUnit.MINUTES])
        assertEquals(23, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

    @Test
    fun `getDuration_duration minus 1 second return 0 day 0 hour 0 minute -1 second`() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.minusSeconds(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(-1, duration[TimeUnit.SECONDS])
        assertEquals(0, duration[TimeUnit.MINUTES])
        assertEquals(0, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

    @Test
    fun `getDuration duration minus 1 minute minus 1 second return 0 day 0 hour -1 minute 1 second`() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.minusMinutes(1).minusSeconds(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(-1, duration[TimeUnit.MINUTES])
        assertEquals(0, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

    @Test
    fun `getDuration duration -1 hour -1 minute -1 second return 0 day -1 hour 1 minute 1 second`() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.minusHours(1).minusMinutes(1).minusSeconds(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(1, duration[TimeUnit.MINUTES])
        assertEquals(-1, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

    @Test
    fun `getDuration duration -1 day -1 hour -1 minute -1 second return -1 day - hour 1 minute 1 second`() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.minusDays(1).minusHours(1).minusMinutes(1).minusSeconds(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(1, duration[TimeUnit.MINUTES])
        assertEquals(1, duration[TimeUnit.HOURS])
        assertEquals(-1L, duration[TimeUnit.DAYS])
    }

    @Test
    fun `addZeroPrefix -10 return -10`() {
        assertEquals("-10", "-10".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix -59 return -59`() {
        assertEquals("-59", "-59".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix -1 return -01`() {
        assertEquals("-01", "-1".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix -9 return -09`() {
        assertEquals("-09", "-9".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix 1 return 01`() {
        assertEquals("01", "1".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix 9 return 09`() {
        assertEquals("09", "9".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix 10 return 10`() {
        assertEquals("10", "10".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix 59 return 59`() {
        assertEquals("59", "59".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix -100 return -100`() {
        assertEquals("-100", "-100".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix 100 return 100`() {
        assertEquals("100", "100".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix -1000 return -1000`() {
        assertEquals("-1000", "-1000".addZeroPrefix())
    }

    @Test
    fun `addZeroPrefix 1000 return 1000`() {
        assertEquals("1000", "1000".addZeroPrefix())
    }

}