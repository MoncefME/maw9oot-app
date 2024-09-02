package com.example.maw9oot.presentation.ui.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.Calendar

fun scheduleDailyNotification(context: Context, calendar: Calendar) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
        putExtra("notification_type", "daily")
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}

fun schedulePrayerReminder(context: Context, prayerTime: Calendar, delayMinutes: Int, prayerName: String) {

    val reminderTime = prayerTime.clone() as Calendar

    reminderTime.add(Calendar.MINUTE, delayMinutes)

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
        putExtra("notification_type", "prayer_reminder")
        putExtra("prayer_name", prayerName)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        prayerTime.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        reminderTime.timeInMillis,
        pendingIntent
    )
}

fun cancelAllNotifications(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, PrayerNotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    alarmManager.cancel(pendingIntent)
}

