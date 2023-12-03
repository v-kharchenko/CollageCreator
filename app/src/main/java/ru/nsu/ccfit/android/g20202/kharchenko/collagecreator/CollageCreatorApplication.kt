package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.CollageDatabase
import ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database.CollageRepository

class CollageCreatorApplication : Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { CollageDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { CollageRepository(database.collageDao()) }

}