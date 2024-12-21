package com.example.weatherapp.fragments.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.fragments.manager_location.ManagerLocationViewModel
import com.example.weatherapp.network.repositoty.WeatherDataRepository

class LocationViewModelFactory(
    private val weatherDataRepository: WeatherDataRepository,
    private val managerLocationViewModel: ManagerLocationViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {

            return LocationViewModel(weatherDataRepository, managerLocationViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
