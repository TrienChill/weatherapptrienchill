package com.example.weatherapp.fragments.manager_location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.RemoteLocation

class ManagerLocationViewModel : ViewModel() {
    private val _locations = MutableLiveData<List<RemoteLocation>>(listOf())
    val locations: LiveData<List<RemoteLocation>> get() = _locations

    fun addLocation(location: RemoteLocation) {
        val updatedList = _locations.value.orEmpty().toMutableList()
        // Kiểm tra trùng lặp trước khi thêm
        if (updatedList.none { it.name == location.name }) {
            updatedList.add(location)
            _locations.value = updatedList
            Log.d("ManagerLocationViewModel", "Location added: ${location.name}")
            Log.d("ManagerLocationViewModel", "Total locations: ${updatedList.size}")
        } else {
            Log.d("ManagerLocationViewModel", "Location already exists: ${location.name}")
        }
    }

    fun toggleFavorite(location: RemoteLocation) {
        val updatedList = _locations.value.orEmpty().map {
            if (it.name == location.name) it.copy(isFavorite = !it.isFavorite) else it
        }
        _locations.value = updatedList
    }

    fun updateLocation(location: RemoteLocation) {
        val updatedList = _locations.value.orEmpty().map {
            if (it.name == location.name) location else it
        }
        _locations.value = updatedList
    }

    fun removeLocation(location: RemoteLocation) {
        val updatedList = _locations.value.orEmpty().toMutableList()
        updatedList.removeAll { it.name == location.name }
        _locations.value = updatedList
        Log.d("ManagerLocationViewModel", "Location removed: ${location.name}")
    }


}
