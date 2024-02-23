package com.elkusnandi.core.common.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class DispatcherModule {

    @Provides
    @Dispatcher(DefaultDispatcher.Default)
    fun provideDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @Provides
    @Dispatcher(DefaultDispatcher.IO)
    fun provideIODispatcher(): CoroutineDispatcher = Dispatchers.IO

}