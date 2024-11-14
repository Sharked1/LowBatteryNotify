package com.example.lowbatterynotify

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

class MyApp: Application() {
    val notificationManager: NotificationManager by lazy {
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannelIfNeeded(this)
    }
}

fun createNotificationChannelIfNeeded(context: Context) {
    // Hanya buat channel jika belum pernah dibuat
    val channel1 = NotificationChannel(
        "battery_channel",
        "Battery Service Notifications",
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        description = "Channel untuk pemberitahuan status baterai"
    }

//    val channel2 = NotificationChannel(
//        "battery_broadcast_channel",
//        "Battery Broadcast Information",
//        NotificationManager.IMPORTANCE_HIGH
//    ).apply {
//        description = "Informasi baterai"
//        enableVibration(true)
//        vibrationPattern = longArrayOf(0,500,2000,500)
//    }
    val channels = listOf(channel1)

    val notificationManager = (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
    notificationManager.createNotificationChannels(channels)
}
