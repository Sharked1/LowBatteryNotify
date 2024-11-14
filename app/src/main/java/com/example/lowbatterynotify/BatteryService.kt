package com.example.lowbatterynotify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.lowbatterynotify.receiver.BatteryReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class BatteryService: Service() {

    private val notificationId = 1
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder

    private val batteryReceiver = BatteryReceiver { status ->
        CoroutineScope(Dispatchers.IO).launch {
            _batteryStatus.emit(status)
        }
    }

    private val _batteryStatus = MutableSharedFlow<BatteryStatus>()
    val batteryStatus = _batteryStatus.asSharedFlow()

    private var batteryLevel = -1
    private var isCharging = false
    private var temperature = -1

    override fun onCreate() {
        super.onCreate()
        notificationManager = (this.applicationContext as MyApp).notificationManager
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        registerReceiver(batteryReceiver, filter)

        CoroutineScope(Dispatchers.Main).launch {
            batteryStatus.collect { status ->
                batteryLevel = status.level
                isCharging = status.isCharging
                temperature = status.temperature
                updateNotification()
            }
        }

        notificationBuilder = NotificationCompat.Builder(this, "battery_channel")
            .setContentTitle("Battery Monitor")
            .setContentText("Charging Battery: $isCharging \nTemperature: $temperature")
            .setSubText("Battery Level: $batteryLevel")
            .setSmallIcon(R.drawable.baseline_battery_5_bar_24)
            .setOngoing(true)
        startForeground(1, notificationBuilder.build())
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(batteryReceiver)
    }

    private fun updateNotification() {
        notificationBuilder
            .setContentText("Charging Battery: $isCharging \nTemperature: $temperature")
            .setSubText("Battery Level: $batteryLevel%")
        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}

data class BatteryStatus(val level: Int, val isCharging: Boolean, val temperature: Int)
