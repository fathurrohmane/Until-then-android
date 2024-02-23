package com.elkusnandi.untilthen.test.unittest

import com.elkusnandi.core.common.getDuration
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class DateTimeUtilKtTest {

    @Test
    fun getDurationTest() {
        val now = ZonedDateTime.now()
        val oneSecondLater = now.plusSeconds(1)

        val duration = oneSecondLater.getDuration(now)

        assertEquals(1, duration[TimeUnit.SECONDS])
        assertEquals(0, duration[TimeUnit.MINUTES])
        assertEquals(0, duration[TimeUnit.HOURS])
        assertEquals(0L, duration[TimeUnit.DAYS])
    }

}