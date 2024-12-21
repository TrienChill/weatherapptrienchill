package com.example.weatherapp.fragments.location

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.RemoteLocation
import com.example.weatherapp.fragments.manager_location.ManagerLocationViewModel
import com.example.weatherapp.network.repositoty.WeatherDataRepository
import kotlinx.coroutines.launch


class LocationViewModel(
    private val weatherDataRepository: WeatherDataRepository,
    private val managerLocationViewModel: ManagerLocationViewModel
) : ViewModel() {
    private val _searchResult = MutableLiveData<SearchResultDataState>()
    val searchResult: LiveData<SearchResultDataState> get() = _searchResult

    private val _saveLocationResult = MutableLiveData<RemoteLocation?>()

    fun searchLocation(query: String) {
        viewModelScope.launch {
            emitSearchResultUiState(isLoading = true)
            val searchResult = weatherDataRepository.searchLocation(query)
            if (searchResult.isNullOrEmpty()) {
                emitSearchResultUiState(error = "No results found")
            } else {
                emitSearchResultUiState(locations = searchResult)
            }
        }
    }

    private fun emitSearchResultUiState(
        isLoading: Boolean = false,
        locations: List<RemoteLocation>? = null,
        error: String? = null
    ) {
        val searchResultDataState = SearchResultDataState(isLoading, locations, error)
        _searchResult.value = searchResultDataState
    }

    data class SearchResultDataState(
        val isLoading: Boolean = false,
        val locations: List<RemoteLocation>?,
        val error: String?
    )

    fun getLocationWeather(remoteLocation: RemoteLocation) {
        viewModelScope.launch {
            val weatherData = weatherDataRepository.getWeatherData(remoteLocation.lat, remoteLocation.lon.toString())
            if (weatherData != null) {
                Log.d("LocationViewModel", "Weather data retrieved successfully")
                val currentWeather = CurrentWeather(
                    icon = weatherData.current.condition.icon,
                    temperature = weatherData.current.temperature,
                    wind = weatherData.current.wind,
                    humidity = weatherData.current.humidity,
                    chanceOfRain = weatherData.forecast.forecastDays.first().day.chanceOfRain
                )

                val locationWithWeather = remoteLocation.copy(currentWeather = currentWeather)
                Log.d("LocationViewModel", "About to add location: ${locationWithWeather.name}")
                managerLocationViewModel.addLocation(locationWithWeather)
                _saveLocationResult.value = locationWithWeather
            } else {
                Log.e("LocationViewModel", "Failed to retrieve weather data")
            }
        }
    }



}