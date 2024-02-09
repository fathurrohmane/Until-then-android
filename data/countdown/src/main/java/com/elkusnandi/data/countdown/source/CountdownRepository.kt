package com.elkusnandi.data.countdown.source

import androidx.paging.PagingData
import com.elkusnandi.data.countdown.model.Countdown
import kotlinx.coroutines.flow.Flow

interface CountdownRepository {
    fun getAllCountdown(): Flow<PagingData<Countdown>>

    suspend fun insertCountdown(countdown: Countdown)

    suspend fun deleteCountdown(countdown: Countdown)

    suspend fun deleteAll()
}