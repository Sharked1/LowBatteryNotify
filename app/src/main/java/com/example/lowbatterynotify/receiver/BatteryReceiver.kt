package com.example.lowbatterynotify.receiver

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.SoundPool
import android.os.BatteryManager
import android.os.CountDownTimer
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.example.lowbatterynotify.BatteryStatus
import com.example.lowbatterynotify.MainActivity
import com.example.lowbatterynotify.MyApp
import com.example.lowbatterynotify.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BatteryReceiver(private val onBatteryStatusChanged: (BatteryStatus) -> Unit) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING
        val batteryStatus = BatteryStatus(level, isCharging, temperature)
        onBatteryStatusChanged(batteryStatus)
    }
}



//    private fun createNotification(context: Context, @DrawableRes icon: Int, intent: PendingIntent, notificationManager: NotificationManager) {
//        val notification = NotificationCompat.Builder(context, "battery_broadcast_channel")
//            .setContentTitle("BatteryStatus: $batteryStatus")
//            .setContentText("Battery Level: $batteryLevel")
//            .setSmallIcon(icon)
//            .setContentIntent(intent)
//            .setAutoCancel(true)
//            .build()
//        notificationManager.notify(notificationid, notification)
//    }
