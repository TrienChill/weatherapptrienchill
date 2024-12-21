package com.example.weatherapp.data

data class RemoteLocation(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val isFavorite: Boolean = false, // Mặc định không yêu thích
    val currentWeather: CurrentWeather? = null // Thêm trường lưu thông tin thời tiết hiện tại

)
