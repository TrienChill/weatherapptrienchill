package com.example.weatherapp.service_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.weatherapp.R

class WeatherUpdateService : Service() {

    override fun onCreate() {
        super.onCreate()
        // Tạo kênh thông báo (dành cho Android 8.0 trở lên)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "weather_service_channel",
                "Weather Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, "weather_service_channel")
            .setContentTitle("Weather Update")
            .setContentText("Fetching the latest weather data.")
            .setSmallIcon(R.drawable.ic_sunny)
            .build()

        // Hiển thị dịch vụ ở chế độ foreground
        startForeground(1, notification)

        // Thực hiện công việc cập nhật thời tiết ở đây
        // Gọi API và cập nhật widget
        // updateWeatherWidget()

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
