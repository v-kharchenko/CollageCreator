package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.collage_editor

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.io.FileNotFoundException

class CollageExporter(private val context: Context) {

    public fun exportCollage(
        name: String,
        dataSet: MutableList<Uri?>,
        rowCount: Int,
        columnCount: Int
    ) {
        val bitmap = getBitmap(dataSet, rowCount, columnCount)
        saveBitmap(name, bitmap)
    }

    private fun getBitmap(dataSet: MutableList<Uri?>, rowCount: Int, columnCount: Int): Bitmap {
        val images = ArrayList<Bitmap>()

        for (uri in dataSet) {
            var image: Bitmap

            if (uri != null) {
                try {
                    val fullImage = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

                    val imageSide = minOf(fullImage.width, fullImage.height)

                    if (fullImage.width >= fullImage.height) {
                        image = Bitmap.createBitmap(
                            fullImage,
                            fullImage.width/2 - fullImage.height/2,
                            0,
                            imageSide,
                            imageSide
                        )
                    }
                    else {
                        image = Bitmap.createBitmap(
                            fullImage,
                            0,
                            fullImage.height/2 - fullImage.width/2,
                            imageSide,
                            imageSide
                        )
                    }
                } catch (ex: FileNotFoundException) {
                    image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                    image.eraseColor(Color.White.toArgb())
                }
            } else {
                image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                image.eraseColor(Color.White.toArgb())
            }

            images.add(image)
        }

        val dimensions = getResultImageDimensions(images, rowCount, columnCount)

        val bitmap = Bitmap.createBitmap(dimensions.first, dimensions.second, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val tileSide = minOf(dimensions.first, dimensions.second) / minOf(columnCount, rowCount)

        for (row in 0 until rowCount) {
            for (column in 0 until columnCount) {
                canvas.drawBitmap(images[row * columnCount + column],
                    null,
                    Rect(column * tileSide, row * tileSide, (column+1)*tileSide, (row+1)*tileSide),
                    Paint()
                )
            }
        }

        return bitmap
    }

    private fun getResultImageDimensions(images: ArrayList<Bitmap>, rowCount: Int, columnCount: Int): Pair<Int, Int> {
        val width: Int
        val height: Int

        if (rowCount >= columnCount) {
            height = 1000
            width = height * columnCount / rowCount
        } else {
            width = 1000
            height = width * rowCount / columnCount
        }

        return Pair(width, height)
    }

    private fun saveBitmap(name: String, bitmap: Bitmap) {
        MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, name, "")
    }

    fun getMiniature(dataSet: MutableList<Uri?>, rowCount: Int, columnCount: Int): Bitmap {
        val images = ArrayList<Bitmap>()

        for (uri in dataSet) {
            var image: Bitmap

            if (uri != null) {
                try {
                    val fullImage = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)

                    val imageSide = minOf(fullImage.width, fullImage.height)

                    if (fullImage.width >= fullImage.height) {
                        image = Bitmap.createBitmap(
                            fullImage,
                            fullImage.width/2 - fullImage.height/2,
                            0,
                            imageSide,
                            imageSide
                        )
                    }
                    else {
                        image = Bitmap.createBitmap(
                            fullImage,
                            0,
                            fullImage.height/2 - fullImage.width/2,
                            imageSide,
                            imageSide
                        )
                    }
                } catch (ex: FileNotFoundException) {
                    image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                    image.eraseColor(Color.White.toArgb())
                }
            } else {
                image = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
                image.eraseColor(Color.White.toArgb())
            }

            images.add(image)
        }

        val dimensions = getMiniatureDimensions(images, rowCount, columnCount)

        val bitmap = Bitmap.createBitmap(dimensions.first, dimensions.second, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val tileSide = minOf(dimensions.first, dimensions.second) / minOf(columnCount, rowCount)

        for (row in 0 until rowCount) {
            for (column in 0 until columnCount) {
                canvas.drawBitmap(images[row * columnCount + column],
                    null,
                    Rect(column * tileSide, row * tileSide, (column+1)*tileSide, (row+1)*tileSide),
                    Paint()
                )
            }
        }

        return bitmap
    }

    private fun getMiniatureDimensions(images: ArrayList<Bitmap>, rowCount: Int, columnCount: Int): Pair<Int, Int> {
        val width: Int
        val height: Int

        if (rowCount >= columnCount) {
            height = 512
            width = height * columnCount / rowCount
        } else {
            width = 512
            height = width * rowCount / columnCount
        }

        return Pair(width, height)
    }
}