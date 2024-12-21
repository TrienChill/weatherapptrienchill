package com.example.weatherapp.fragments.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetProvider
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R
import org.json.JSONObject

class WeatherWidgetProvider : AppWidgetProvider() {

    companion object {
        private const val TAG = "WeatherWidgetProvider"
        const val ACTION_WIDGET_REFRESH = "com.example.weatherapp.WIDGET_REFRESH"
    }

    // Update the widget
    private fun updateWeatherWidget(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int
    ) {
        Log.d(TAG, "updateWeatherWidget called for appWidgetId: $appWidgetId")

        val views = RemoteViews(context.packageName, R.layout.widget_weather)

        // Retrieve the weather data from WeatherPrefs
        val weatherPrefs = context.getSharedPreferences("WeatherPrefs", Context.MODE_PRIVATE)
        val icon = weatherPrefs.getString("weatherIcon", "sunny") // Default icon if not found
        val temperature = weatherPrefs.getInt("temperature", 0)

        // Retrieve the location data from WeatherAppPrefs (in JSON format)
        val appPrefs = context.getSharedPreferences("WeatherAppPrefs", Context.MODE_PRIVATE)
        val currentLocationJson = appPrefs.getString("current_location", "{}")
        val locationName = getLocationFromJson(currentLocationJson.toString())

        // Update the weather data if available
        if (icon != null && temperature != 0) {
            Log.d(TAG, "Weather data retrieved from SharedPreferences: icon = $icon, temperature = $temperature")
            val iconResourceId = getWeatherIconResourceId(icon)
            Log.d(TAG, "Icon resource ID: $iconResourceId")
            views.setImageViewResource(R.id.widgetWeatherIcon, iconResourceId)
            views.setTextViewText(R.id.widgetTemperatureText, "$temperature°C")
            views.setTextViewText(R.id.widgetLocationText, locationName) // Set the location name in the widget
        }

        // Main Intent to navigate to HomeFragment
        val mainIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("openHomeFragment", true) // Indicate to open HomeFragment
        }
        val mainPendingIntent = PendingIntent.getActivity(
            context,
            0,
            mainIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // Set the onClickPendingIntent for the entire widget to trigger both refresh and navigation
        views.setOnClickPendingIntent(R.id.widgetClickableArea, mainPendingIntent)

        // Update the widget
        appWidgetManager?.updateAppWidget(appWidgetId, views)
        Log.d(TAG, "Widget updated for appWidgetId: $appWidgetId")
    }

    // Function to parse the JSON and extract the location name
    private fun getLocationFromJson(jsonString: String): String {
        return try {
            val jsonObject = JSONObject(jsonString)
            jsonObject.optString("location", "Unknown Location") // Default to "Unknown Location" if not found
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing location JSON: ${e.message}")
            "Unknown Location" // Default in case of error
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        Log.d(TAG, "onReceive called with action: ${intent?.action}")

        when (intent?.action) {
            ACTION_WIDGET_REFRESH, AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                // Refresh the widget when action is triggered
                Log.d(TAG, "Widget refresh/update action received")
                context?.let { ctx ->
                    val appWidgetManager = AppWidgetManager.getInstance(ctx)
                    val componentName = ComponentName(ctx, WeatherWidgetProvider::class.java)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
                    onUpdate(ctx, appWidgetManager, appWidgetIds)
                }
            }
            "com.example.weatherapp.WEATHER_DATA_UPDATED" -> {
                // When the broadcast WEATHER_DATA_UPDATED is received, update the widget with new data
                context?.let { ctx ->
                    val appWidgetManager = AppWidgetManager.getInstance(ctx)
                    val componentName = ComponentName(ctx, WeatherWidgetProvider::class.java)
                    val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
                    appWidgetIds.forEach { appWidgetId ->
                        updateWeatherWidget(ctx, appWidgetManager, appWidgetId)
                    }
                }
            }
        }
    }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        Log.d(TAG, "onUpdate called")
        context?.let { ctx ->
            appWidgetIds?.forEach { appWidgetId ->
                Log.d(TAG, "Updating widget with appWidgetId: $appWidgetId")
                updateWeatherWidget(ctx, appWidgetManager, appWidgetId)
            }
        }
    }

    private fun getWeatherIconResourceId(icon: String): Int {
        Log.d(TAG, "getWeatherIconResourceId called for icon: $icon")
        return when (icon) {
            "sunny" -> R.drawable.ic_sunny
            "cloudy" -> R.drawable.ic_cloudy
            "rainy" -> R.drawable.ic_rainy
            // Add other weather conditions as necessary
            else -> R.drawable.ic_sunny // Default icon if no match
        }
    }
}
