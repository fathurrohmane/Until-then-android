package com.elkusnandi.data.countdown.di

import android.content.Context
import androidx.room.Room
import com.elkusnandi.data.countdown.source.local.LocalCountdownDataSource
import com.elkusnandi.data.countdown.source.local.RoomLocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

const val database_name = "database.db"

@Module
@InstallIn(SingletonComponent::class)
internal class DatabaseModule {

    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context): RoomLocalDatabase {
        return Room.databaseBuilder(context, RoomLocalDatabase::class.java, database_name)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideUserDataSource(database: RoomLocalDatabase): LocalCountdownDataSource {
        return database.countdownDao()
    }
}