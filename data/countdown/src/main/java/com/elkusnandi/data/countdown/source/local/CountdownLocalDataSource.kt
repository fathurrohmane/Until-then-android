package com.elkusnandi.data.countdown.source.local

import androidx.paging.PagingSource
import com.elkusnandi.data.countdown.source.local.model.CountdownEntity

internal interface CountdownLocalDataSource {

    fun getAllCountdown(): PagingSource<Int, CountdownEntity>

    suspend fun insertCountdown(countdown: CountdownEntity)

    suspend fun deleteCountdown(countdown: CountdownEntity)

    suspend fun deleteAll()

}