package com.example.maw9oot.presentation.ui.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.maw9oot.MainActivity
import com.example.maw9oot.R

class PrayerNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationType = intent.getStringExtra("notification_type")

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED) {

            val launchIntent = Intent(context, MainActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(
                context, 0, launchIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val channelId = "prayer_notification_channel"
            val name = "Prayer Notifications"
            val descriptionText = "Notification for prayer times"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.none)
                .setContentTitle(
                    when (notificationType) {
                        "daily" -> "Daily Prayer Reminder"
                        "prayer_reminder" -> "Prayer Time Reminder"
                        else -> "Reminder"
                    }
                )
                .setContentText(
                    when (notificationType) {
                        "daily" -> "It's time to log your prayers for today."
                        "prayer_reminder" -> "Don't forget to log your prayer status."
                        else -> "Reminder to log your prayer status."
                    }
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                notify(notificationType?.hashCode() ?: 1, notificationBuilder.build())
            }
        } else {
            Log.d("PrayerNotificationReceiver", "Notification permission not granted")
        }
    }
}
