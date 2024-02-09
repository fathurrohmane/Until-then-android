package com.elkusnandi.core.common.di

import com.elkusnandi.data.di.DefaultDispatcher
import com.elkusnandi.data.di.Dispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlin.text.Typography.dagger

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