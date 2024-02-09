package com.elkusnandi.data.countdown.source

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.elkusnandi.data.countdown.model.Countdown
import com.elkusnandi.data.countdown.model.toEntity
import com.elkusnandi.data.countdown.source.local.CountdownLocalDataSource
import com.elkusnandi.data.countdown.source.local.model.CountdownEntity
import com.elkusnandi.data.countdown.source.local.model.toModel
import com.elkusnandi.data.di.DefaultDispatcher
import com.elkusnandi.data.di.Dispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class DefaultCountdownRepository @Inject constructor(
    private val localDataSource: CountdownLocalDataSource,
    @Dispatcher(DefaultDispatcher.IO) val defaultDispatcher: CoroutineDispatcher
) : CountdownRepository {

    override fun getAllCountdown() =
        Pager(
            config = PagingConfig(20),
            pagingSourceFactory = {
                localDataSource.getAllCountdown()
            }
        ).flow
            .map { value: PagingData<CountdownEntity> ->
                value.map { entity ->
                    entity.toModel()
                }
            }.flowOn(defaultDispatcher)

    override suspend fun insertCountdown(countdown: Countdown) {
        localDataSource.insertCountdown(countdown.toEntity())
    }

    override suspend fun deleteCountdown(countdown: Countdown) {
        localDataSource.deleteCountdown(countdown.toEntity())
    }

    override suspend fun deleteAll() {
        localDataSource.deleteAll()
    }

}