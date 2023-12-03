package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = arrayOf(Collage::class), version = 1, exportSchema = false)
abstract class CollageDatabase : RoomDatabase() {

    abstract fun collageDao(): CollageDao

    private class CollageDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var collageDao = database.collageDao()

                    var collage = Collage("Collage One", 3, 3)
                    collageDao.insert(collage)

                    collage = Collage("Collage Two", 3, 3)
                    collageDao.insert(collage)

                    collage = Collage("Collage Three", 3, 3)
                    collageDao.insert(collage)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: CollageDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CollageDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CollageDatabase::class.java,
                    "collage_database"
                )
                    .addCallback(CollageDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}