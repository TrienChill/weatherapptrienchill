package com.example.weatherapp.network.api

import com.example.weatherapp.data.RemoteLocation
import com.example.weatherapp.data.RemoteWeatherData
import com.example.weatherapp.data.RemoteWeatherFullData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    companion object{
        const val BASE_URL = "https://api.weatherapi.com/v1/"
        const val API_KEY = ""
    }
    @GET("search.json")
    suspend fun searchLocation(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String
    ): Response<List<RemoteLocation>>

    @GET("forecast.json")
    suspend fun getWeatherData(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String
    ): Response<RemoteWeatherData>

    // Thêm phương thức mới phần này có thể tự nâng cấp về sau
    @GET("current.json")
    suspend fun getRemoteWeatherFullData(
        @Query("key") key: String = API_KEY,
        @Query("q") query: String
    ): Response<RemoteWeatherFullData>
}