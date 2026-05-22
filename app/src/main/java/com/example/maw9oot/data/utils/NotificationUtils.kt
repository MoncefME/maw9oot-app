package com.example.maw9oot.data.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.example.maw9oot.data.local.DataStoreManager
import com.example.maw9oot.data.repository.PrayerTimesRepository
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val DAILY_NOTIFICATION_REQUEST_CODE = 1000

// Coordinates for prayer times (Algiers, Algeria)
private const val DEFAULT_LATITUDE = 36.402482
private const val DEFAULT_LONGITUDE = 3.323412

fun scheduleDailyNotification(context: Context, calendar: Calendar) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
        putExtra("notification_type", "daily")
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        DAILY_NOTIFICATION_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        Log.d("NotificationUtils", "Daily notification scheduled (Inexact) for ${calendar.time}")
    } else {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
        Log.d("NotificationUtils", "Daily notification scheduled (Exact) for ${calendar.time}")
    }
}

fun schedulePrayerReminder(context: Context, prayerTime: Calendar, delayMinutes: Int, prayerName: String) {

    val reminderTime = prayerTime.clone() as Calendar
    reminderTime.add(Calendar.MINUTE, delayMinutes)
    reminderTime.set(Calendar.SECOND, 0)
    reminderTime.set(Calendar.MILLISECOND, 0)

    // Ensure we are scheduling for the future
    if (reminderTime.timeInMillis <= System.currentTimeMillis()) {
        Log.d("NotificationUtils", "Skipping past prayer reminder for $prayerName at ${reminderTime.time}")
        return
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
        putExtra("notification_type", "prayer_reminder")
        putExtra("prayer_name", prayerName)
    }

    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(reminderTime.time)
    val requestCode = "${prayerName}_${formattedDate}".hashCode()

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
        alarmManager.setAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminderTime.timeInMillis,
            pendingIntent
        )
        Log.d("NotificationUtils", "Prayer reminder scheduled (Inexact) for $prayerName at ${reminderTime.time}")
    } else {
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            reminderTime.timeInMillis,
            pendingIntent
        )
        Log.d("NotificationUtils", "Prayer reminder scheduled (Exact) for $prayerName at ${reminderTime.time}")
    }
}

fun cancelDailyNotification(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
        putExtra("notification_type", "daily")
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        DAILY_NOTIFICATION_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    )
    if (pendingIntent != null) {
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
        Log.d("NotificationUtils", "Daily notification cancelled")
    }
}

fun cancelPrayerReminders(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")

    // We need to cancel for today and tomorrow at least
    val datesToCancel = listOf(
        Calendar.getInstance(),
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
    )
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    for (dateCalendar in datesToCancel) {
        val formattedDate = dateFormat.format(dateCalendar.time)
        for (prayerName in prayers) {
            val requestCode = "${prayerName}_${formattedDate}".hashCode()
            val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
                putExtra("notification_type", "prayer_reminder")
                putExtra("prayer_name", prayerName)
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            if (pendingIntent != null) {
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()
                Log.d("NotificationUtils", "Prayer reminder cancelled for $prayerName on $formattedDate")
            }
        }
    }
}

fun cancelAllNotifications(context: Context) {
    cancelDailyNotification(context)
    cancelPrayerReminders(context)
}

suspend fun rescheduleAllAlarms(
    context: Context,
    dataStoreManager: DataStoreManager,
    prayerTimesRepository: PrayerTimesRepository
) {
    Log.d("NotificationUtils", "Rescheduling all alarms...")

    // Schedule Daily Notification
    val isDailyEnabled = dataStoreManager.isDailyNotificationEnabled.first()
    if (isDailyEnabled) {
        val time = dataStoreManager.notificationTime.first()
        try {
            val (hour, minute) = time.split(":").map { it.toInt() }
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                if (timeInMillis <= System.currentTimeMillis()) {
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            scheduleDailyNotification(context, calendar)
        } catch (e: Exception) {
            Log.e("NotificationUtils", "Error parsing daily notification time: $time", e)
        }
    } else {
        cancelDailyNotification(context)
    }

    // Ensure prayer times are synced for today and tomorrow
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val datesToSchedule = listOf(
        Calendar.getInstance(),
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
    )

    for (dateCalendar in datesToSchedule) {
        val formattedDate = dateFormat.format(dateCalendar.time)
        if (prayerTimesRepository.getPrayerTimesForDate(formattedDate).isEmpty()) {
            val year = dateCalendar.get(Calendar.YEAR)
            try {
                prayerTimesRepository.fetchAndStorePrayerTimes(DEFAULT_LATITUDE, DEFAULT_LONGITUDE, year)
                Log.d("NotificationUtils", "Prayer times synced automatically for $formattedDate")
            } catch (e: Exception) {
                Log.e("NotificationUtils", "Error syncing prayer times automatically: ${e.message}")
            }
        }
    }

    // Schedule Prayer Reminders
    val isPrayerEnabled = dataStoreManager.isPrayerReminderEnabled.first()
    if (isPrayerEnabled) {
        val delay = dataStoreManager.prayerReminderDelay.first().toIntOrNull() ?: 15

        for (dateCalendar in datesToSchedule) {
            val formattedDate = dateFormat.format(dateCalendar.time)
            val prayerTimes = prayerTimesRepository.getPrayerTimesForDate(formattedDate)

            for (prayerTime in prayerTimes) {
                try {
                    // Robustly parse time like "05:30 (CET)"
                    val timeStr = prayerTime.time.split(" ")[0]
                    val timeParts = timeStr.split(":")
                    if (timeParts.size >= 2) {
                        val calendar = Calendar.getInstance().apply {
                            time = dateCalendar.time
                            set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
                            set(Calendar.MINUTE, timeParts[1].toInt())
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }

                        schedulePrayerReminder(context, calendar, delay, prayerTime.prayerName)
                    }
                } catch (e: Exception) {
                    Log.e("NotificationUtils", "Error parsing prayer time: ${prayerTime.time} for ${prayerTime.prayerName}", e)
                }
            }
        }
    } else {
        cancelPrayerReminders(context)
    }
}
