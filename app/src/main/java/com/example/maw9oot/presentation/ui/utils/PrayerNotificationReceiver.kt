package com.example.maw9oot.presentation.ui.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.maw9oot.MainActivity
import com.example.maw9oot.R
import com.example.maw9oot.presentation.ui.enums.Prayer

class PrayerNotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationType = intent.getStringExtra("notification_type") ?: "prayer_reminder"
        val prayerName = intent.getStringExtra("prayer_name") ?: "Prayer"

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
            == PackageManager.PERMISSION_GRANTED
        ) {

            val launchIntent = Intent(context, MainActivity::class.java).apply {
                putExtra("from_notification", true)
                putExtra("prayer_name", prayerName)
            }
            val pendingIntent = PendingIntent.getActivity(
                context,
                prayerName.hashCode(),
                launchIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val channelId = "prayer_notification_channel"
            val channelName = "Prayer Notifications"
            val descriptionText = "Notification for prayer times"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)

            val contentTitle: String
            val contentText: String
            val notificationId: Int

            when (notificationType) {
                "daily" -> {
                    contentTitle = "Daily Prayer Reminder"
                    contentText = "It's time to log your prayers for today."
                    notificationId = "daily".hashCode()
                }

                "prayer_reminder" -> {
                    contentTitle = "$prayerName Reminder"
                    contentText =
                        "It's time for $prayerName. Don't forget to log your prayer status."
                    notificationId = prayerName.hashCode()
                }

                else -> {
                    contentTitle = "Reminder"
                    contentText = "Reminder to log your prayer status."
                    notificationId = notificationType.hashCode()
                }
            }


            val largeIcon = if (notificationType === "prayer_reminder")
                BitmapFactory.decodeResource(context.resources, Prayer.fromName(prayerName).icon)
            else
                BitmapFactory.decodeResource(context.resources, R.drawable.baseline_notifications_active_24)
            val notificationBuilder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.mini_logo)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setLargeIcon(largeIcon)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, notificationBuilder.build())
            }
        } else {
            Log.d("PrayerNotificationReceiver", "Notification permission not granted")
        }
    }
}
