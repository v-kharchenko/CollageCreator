package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database

import android.net.Uri
import androidx.room.TypeConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun toList(value: String): List<Uri?> {
        return value.split(",").map {
            if (it == "null")
                null
            else
                Uri.parse(it)
        }
    }

    @TypeConverter
    fun fromList(list: List<Uri?>): String {
        return list.joinToString(separator = ",") { it?.toString() ?: "null" }
    }
}