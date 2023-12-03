package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CollageDao {

    @Query("SELECT * FROM collage_table")
    fun getCollages(): Flow<List<Collage>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(collage: Collage)

    @Query("DELETE FROM collage_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM collage_table WHERE id = :id")
    fun getCollageById(id: Int): LiveData<Collage>
}