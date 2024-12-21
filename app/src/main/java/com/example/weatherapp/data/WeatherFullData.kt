package com.example.weatherapp.data

import java.text.SimpleDateFormat
import java.util.Locale

data class WeatherFullData(
    val location: LocationData,
    val currentWeather: CurrentWeatherData
)

data class LocationData(
    val name: String,
    val region: String,
    val country: String,
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val localtime: String
)

data class CurrentWeatherData(
    val lastUpdated: String,
    val temperatureC: Float,
    val temperatureF: Float,
    val conditionText: String,
    val conditionIcon: String,
    val windKph: Float,
    val humidity: Int,
    val cloud: Int,
    val feelsLikeC: Float
)

// Helper function to format the localtime in a user-friendly way
fun formatLocaltime(localtime: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    val outputFormat = SimpleDateFormat("EEEE, dd MMM yyyy HH:mm", Locale.getDefault())
    val date = inputFormat.parse(localtime)
    return outputFormat.format(date ?: "")
}
