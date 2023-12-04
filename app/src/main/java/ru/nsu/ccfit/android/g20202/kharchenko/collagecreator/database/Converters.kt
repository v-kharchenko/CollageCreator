package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import androidx.room.TypeConverter
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

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

    @TypeConverter
    fun toBitmap(bytes: ByteArray): Bitmap? {
        if (bytes.size == 1)
            return null

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    @TypeConverter
    fun fromBitmap(bmp: Bitmap?): ByteArray {
        val outputStream = ByteArrayOutputStream()

        if (bmp == null) {
            outputStream.write(0)
        } else {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }

        return outputStream.toByteArray()
    }
}