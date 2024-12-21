package com.example.weatherapp.fragments.home

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.CurrentLocation
import com.example.weatherapp.data.CurrentWeather
import com.example.weatherapp.data.Forecast
import com.example.weatherapp.data.LiveDataEvent
import com.example.weatherapp.network.repositoty.WeatherDataRepository
import com.example.weatherapp.utils.NotificationHelper
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel(private val weatherDataRepository: WeatherDataRepository

) : ViewModel(){

    //region Current location
    private val _currentLocation = MutableLiveData< LiveDataEvent<CurrentLocationDataState>>()
    val currentLocation: LiveData<LiveDataEvent<CurrentLocationDataState>> get() = _currentLocation


    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        geocoder: Geocoder
    ) {
        viewModelScope.launch {
            emitCurrentLocationUiState(isLoading = true)
            weatherDataRepository.getCurrentLocation(
                fusedLocationProviderClient = fusedLocationProviderClient,
                onSuccess = { currentLocation ->
                        updateAddressText(currentLocation, geocoder)
                },
                onFailure = {
                    emitCurrentLocationUiState(error = "Failed to get current location")
                }
            )
        }
    }


    private fun updateAddressText(currentLocation: CurrentLocation, geocoder: Geocoder)  {
        viewModelScope.launch {
            runCatching {
                weatherDataRepository.updateAddressText(currentLocation, geocoder)
            }.onSuccess {location ->
                emitCurrentLocationUiState(currentLocation = location)

            }.onFailure {
                emitCurrentLocationUiState(
                    currentLocation = currentLocation.copy(
                        location = "Unknown"
                    )
                )
            }
        }
    }
    private fun emitCurrentLocationUiState(
        isLoading: Boolean = false,
        currentLocation: CurrentLocation? = null,
        error: String? = null
    ) {
        val currentLocationDataState = CurrentLocationDataState(
            isLoading = isLoading,
            currentLocation = currentLocation,
            error = error
        )
        _currentLocation.value = LiveDataEvent(currentLocationDataState)
    }


    data class CurrentLocationDataState(
        val isLoading: Boolean,
        val currentLocation: CurrentLocation?,
        val error: String?
    )

    //endregion

    //region Weather data
    private val _weatherData = MutableLiveData<LiveDataEvent<WeatherDataState>>()
    val weatherData: LiveData<LiveDataEvent<WeatherDataState>> get() = _weatherData

    fun getWeatherData(latitude: Double, longitude: String) {
        viewModelScope.launch {
            emitWeatherDataUiState(isLoading = true)
            weatherDataRepository.getWeatherData(latitude, longitude)?.let { weatherData ->
                emitWeatherDataUiState(
                    currentWeather = CurrentWeather(
                        icon = weatherData.current.condition.icon,
                        temperature = weatherData.current.temperature,
                        wind = weatherData.current.wind,
                        humidity = weatherData.current.humidity,
                        chanceOfRain = weatherData.forecast.forecastDays.first().day.chanceOfRain
                    ),
                    forecast = weatherData.forecast.forecastDays.first().hour.map{
                        Forecast(
                            time = getForecastTime(it.time),
                            temperature = it.temperature,
                            feelsLikeTemperature = it.feelsLikeTemperature,
                            icon = it.condition.icon
                        )
                    }
                )

            }?: emitWeatherDataUiState(error = "Failed to get weather data")
        }
    }

    private fun emitWeatherDataUiState(
        isLoading: Boolean = false,
        currentWeather: CurrentWeather? = null,
        forecast: List<Forecast>? = null,
        error: String? = null
    ) {
        val weatherDataState = WeatherDataState(isLoading, currentWeather, forecast ,error)
        _weatherData.value = LiveDataEvent(weatherDataState)
    }

    data class WeatherDataState(
        val isLoading: Boolean,
        val currentWeather: CurrentWeather?,
        val forecast: List<Forecast>?,
        val error: String?
    )

    private fun getForecastTime(datetime: String): String {
        val pattern = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = pattern.parse(datetime)?: return datetime
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)

    }
    //endregion

    //region Weather data context và thêm nhiều phần khác full data
    fun getWeatherDataWithContext(latitude: Double, longitude: String, context: Context) {
        viewModelScope.launch {
            emitWeatherDataUiState(isLoading = true)
            weatherDataRepository.getWeatherData(latitude, longitude)?.let { weatherData ->
                val currentWeather = CurrentWeather(
                    icon = weatherData.current.condition.icon,
                    temperature = weatherData.current.temperature,
                    wind = weatherData.current.wind,
                    humidity = weatherData.current.humidity,
                    chanceOfRain = weatherData.forecast.forecastDays.first().day.chanceOfRain
                )

                // Save weather data to SharedPreferences
                saveWeatherDataToSharedPrefs(context, currentWeather)

                // Cập nhật LiveData
                emitWeatherDataUiState(
                    currentWeather = currentWeather,
                    forecast = weatherData.forecast.forecastDays.first().hour.map {
                        Forecast(
                            time = getForecastTime(it.time),
                            temperature = it.temperature,
                            feelsLikeTemperature = it.feelsLikeTemperature,
                            icon = it.condition.icon
                        )
                    }
                )
                // Hiển thị thông báo thời tiết
                sendWeatherNotification(context, "Vị trí của bạn", currentWeather)
                // Gửi Broadcast
                sendWeatherDataBroadcast(context, currentWeather)
            } ?: emitWeatherDataUiState(error = "Failed to get weather data")
        }
    }






    private fun saveWeatherDataToSharedPrefs(context: Context, currentWeather: CurrentWeather) {
        val sharedPreferences = context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        editor.putString("weatherIcon", currentWeather.icon)
        editor.putInt("temperature", currentWeather.temperature.toInt())
        editor.putString("weatherName", "Current Weather") // Or any other name you want to store
        editor.apply()

        Log.d(TAG, "Weather data saved to SharedPreferences: Icon=${currentWeather.icon}, Temp=${currentWeather.temperature}")
    }

    private fun sendWeatherDataBroadcast(context: Context, currentWeather: CurrentWeather) {
        val intent = Intent("com.example.weatherapp.WEATHER_DATA_UPDATED")
        intent.putExtra("icon", currentWeather.icon)
        intent.putExtra("temperature", currentWeather.temperature)
        context.sendBroadcast(intent)
    }


    //endregion

    //region Thông báo
    private fun sendWeatherNotification(context: Context, location: String, currentWeather: CurrentWeather) {
        val notificationHelper = NotificationHelper(context)
        val forecast = "Nhiệt độ: ${currentWeather.temperature}°C, Độ ẩm: ${currentWeather.humidity}%, Gió: ${currentWeather.wind} km/h"
        notificationHelper.showWeatherNotification(
            location = location,
            status = "Hiện tại: ${currentWeather.temperature}°C",
            forecast = forecast
        )
    }
    //endregion

}