package com.example.weatherapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.weatherapp.MainActivity
import com.example.weatherapp.R

class NotificationHelper(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "weather_notifications"
        const val CHANNEL_NAME = "Weather Notifications"
        const val CHANNEL_DESCRIPTION = "Notifications about current weather and forecast changes"
        const val NOTIFICATION_ID = 1
    }

    init {
        createNotificationChannel()
    }

    // Tạo Notification Channel (chỉ cho Android 8.0+)
    private fun createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = CHANNEL_DESCRIPTION
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Hiển thị thông báo
    fun showWeatherNotification(location: String, status: String, forecast: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_weather_notification) // Thay thế icon phù hợp
            .setContentTitle("Thời tiết tại $location")
            .setContentText("Hiện tại: $status. Dự báo: $forecast.")
            .setStyle(
                NotificationCompat.BigTextStyle().bigText("Thời tiết tại $location\nHiện tại: $status\nDự báo: $forecast")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
}
