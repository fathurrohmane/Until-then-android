package com.elkusnandi.data.countdown.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.elkusnandi.data.countdown.model.Countdown
import java.time.ZonedDateTime

@Entity
internal data class CountdownEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo
    val title: String,
    @ColumnInfo(name = "data_time")
    val dateTime: ZonedDateTime,
    val color: String
)

internal object ZonedDateTimeTypeConverter {
    @TypeConverter
    fun fromString(value: String): ZonedDateTime {
        return ZonedDateTime.parse(value)
    }

    @TypeConverter
    fun toString(dateTime: ZonedDateTime): String {
        return dateTime.toString()
    }
}

internal fun CountdownEntity.toModel() = Countdown(
    id, title, dateTime, color
)