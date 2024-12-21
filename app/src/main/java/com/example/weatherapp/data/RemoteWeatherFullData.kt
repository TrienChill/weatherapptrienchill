package com.example.weatherapp.data
import com.google.gson.annotations.SerializedName

data class RemoteWeatherFullData(
    val location: LocationRemote,
    val current: CurrentWeatherRemoteFull
)

data class LocationRemote(
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("tz_id") val timezone: String,
    @SerializedName("localtime_epoch") val localtimeEpoch: Long,
    @SerializedName("localtime") val localtime: String
)

data class CurrentWeatherRemoteFull(
    @SerializedName("last_updated_epoch") val lastUpdatedEpoch: Long,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("temp_c") val temperatureC: Float,
    @SerializedName("temp_f") val temperatureF: Float,
    @SerializedName("is_day") val isDay: Int,
    val condition: WeatherConditionRemoteFull,
    @SerializedName("wind_mph") val windMph: Float,
    @SerializedName("wind_kph") val windKph: Float,
    @SerializedName("wind_degree") val windDegree: Int,
    @SerializedName("wind_dir") val windDir: String,
    @SerializedName("pressure_mb") val pressureMb: Float,
    @SerializedName("pressure_in") val pressureIn: Float,
    @SerializedName("precip_mm") val precipMm: Float,
    @SerializedName("precip_in") val precipIn: Float,
    val humidity: Int,
    val cloud: Int,
    @SerializedName("feelslike_c") val feelsLikeC: Float,
    @SerializedName("feelslike_f") val feelsLikeF: Float,
    @SerializedName("windchill_c") val windChillC: Float,
    @SerializedName("windchill_f") val windChillF: Float,
    @SerializedName("heatindex_c") val heatIndexC: Float,
    @SerializedName("heatindex_f") val heatIndexF: Float,
    @SerializedName("dewpoint_c") val dewPointC: Float,
    @SerializedName("dewpoint_f") val dewPointF: Float,
    @SerializedName("vis_km") val visibilityKm: Float,
    @SerializedName("vis_miles") val visibilityMiles: Float,
    val uv: Float,
    @SerializedName("gust_mph") val gustMph: Float,
    @SerializedName("gust_kph") val gustKph: Float
)

data class WeatherConditionRemoteFull(
    val text: String,
    val icon: String,
    val code: Int
)
