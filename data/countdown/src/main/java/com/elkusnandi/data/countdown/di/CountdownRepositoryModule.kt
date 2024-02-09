package com.elkusnandi.data.countdown.di

import com.elkusnandi.data.countdown.source.CountdownRepository
import com.elkusnandi.data.countdown.source.DefaultCountdownRepository
import com.elkusnandi.data.countdown.source.local.LocalCountdownDataSource
import com.elkusnandi.data.di.DefaultDispatcher
import com.elkusnandi.data.di.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    fun provideRepository(
        localDataSource: LocalCountdownDataSource,
        @Dispatcher(DefaultDispatcher.IO) defaultDispatcher: CoroutineDispatcher
    ): CountdownRepository =
        DefaultCountdownRepository(localDataSource, defaultDispatcher)
}