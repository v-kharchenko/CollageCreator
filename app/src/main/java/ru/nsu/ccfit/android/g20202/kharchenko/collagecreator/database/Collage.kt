package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database

import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "collage_table")
data class Collage(
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "column_count") var columnCount: Int = 0,
    @ColumnInfo(name = "row_count") var rowCount: Int = 0
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
    @ColumnInfo(name = "uri_list") var uriList: List<Uri?> = ArrayList<Uri?>()
    @ColumnInfo(name = "miniature") var image: Bitmap? = null
}