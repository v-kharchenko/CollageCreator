package ru.nsu.ccfit.android.g20202.kharchenko.collagecreator.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CollageViewModel(private val repository: CollageRepository) : ViewModel() {
    val allCollages: LiveData<List<Collage>> = repository.allCollages.asLiveData()

    fun insert(collage: Collage) = viewModelScope.launch {
        repository.insert(collage)
    }

    fun getCollageById(id: Int): LiveData<Collage> {
        return repository.getCollageById(id)
    }
}

class CollageViewModelFactory(private val repository: CollageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CollageViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}