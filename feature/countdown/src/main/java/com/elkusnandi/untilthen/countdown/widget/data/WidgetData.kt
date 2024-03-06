package com.elkusnandi.untilthen.countdown.widget.data

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import androidx.glance.state.GlanceStateDefinition
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object WidgetDataStateDefinition : GlanceStateDefinition<WidgetConfig> {

    private const val DATA_STORE_FILENAME = "widgetConfig_"

    override suspend fun getDataStore(context: Context, fileKey: String) = DataStoreFactory.create(
        serializer = ClockDataSerializer,
        produceFile = { getLocation(context, fileKey) }
    )

    override fun getLocation(context: Context, fileKey: String) =
        context.dataStoreFile(DATA_STORE_FILENAME + fileKey.lowercase())

    /**
     * Custom serializer for WeatherInfo using Json.
     */
    object ClockDataSerializer : Serializer<WidgetConfig> {
        override val defaultValue = WidgetConfig()

        override suspend fun readFrom(input: InputStream): WidgetConfig = try {
            Json.decodeFromString(
                WidgetConfig.serializer(),
                input.readBytes().decodeToString()
            )
        } catch (exception: SerializationException) {
            throw CorruptionException("Could not read weather data: ${exception.message}")
        }

        override suspend fun writeTo(t: WidgetConfig, output: OutputStream) {
            output.use {
                it.write(
                    Json.encodeToString(WidgetConfig.serializer(), t).encodeToByteArray()
                )
            }
        }
    }
}