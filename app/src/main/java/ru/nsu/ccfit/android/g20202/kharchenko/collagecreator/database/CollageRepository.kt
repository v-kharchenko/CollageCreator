package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class CollageRepository(private val collageDao: CollageDao) {
    val allCollages: Flow<List<Collage>> = collageDao.getCollages()

    @WorkerThread
    suspend fun insert(collage: Collage) {
        collageDao.insert(collage)
    }

    fun getCollageById(id: Int): LiveData<Collage> {
        return collageDao.getCollageById(id)
    }
}