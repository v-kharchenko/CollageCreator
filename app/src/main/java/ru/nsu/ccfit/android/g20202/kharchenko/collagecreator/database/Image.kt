package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database

import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class Image(
    @PrimaryKey(autoGenerate = true)  var id: Int = 0,
    @ColumnInfo(name = "collage_id") var collageId: Int = 0,
    @ColumnInfo(name = "uri") var uri: Uri? = null
)