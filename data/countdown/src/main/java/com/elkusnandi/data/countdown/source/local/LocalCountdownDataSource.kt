package com.elkusnandi.data.countdown.source.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.Upsert
import com.elkusnandi.data.countdown.source.local.model.CountdownEntity
import com.elkusnandi.data.countdown.source.local.model.ZonedDateTimeTypeConverter

@Dao
internal interface LocalCountdownDataSource : CountdownLocalDataSource {

    @Query("SELECT * FROM CountdownEntity")
    override fun getAllCountdown(): PagingSource<Int, CountdownEntity>

    @Upsert
    override suspend fun insertCountdown(countdown: CountdownEntity)

    @Delete
    override suspend fun deleteCountdown(countdown: CountdownEntity)

    @Query("DELETE FROM CountdownEntity")
    override suspend fun deleteAll()

}

@Database(
    entities = [CountdownEntity::class],
    version = 1
)
@TypeConverters(ZonedDateTimeTypeConverter::class)
internal abstract class RoomLocalDatabase : RoomDatabase() {
    abstract fun countdownDao(): LocalCountdownDataSource
}