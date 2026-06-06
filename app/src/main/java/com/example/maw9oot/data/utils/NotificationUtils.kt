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

fun scheduleDailyNotification(context: Context, calendar: Calendar) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            return
        }
    }

    val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
        putExtra("notification_type", "daily")
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        DAILY_NOTIFICATION_REQUEST_CODE,
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

    // Ensure we are scheduling for the future
    if (reminderTime.before(Calendar.getInstance())) {
        return
    }

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (!alarmManager.canScheduleExactAlarms()) {
            // We don't want to spam the user with permission requests if scheduling multiple reminders
            // The daily notification schedule should handle the initial request
            return
        }
    }

    val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
        putExtra("notification_type", "prayer_reminder")
        putExtra("prayer_name", prayerName)
    }

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        prayerName.hashCode(),
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    alarmManager.setExactAndAllowWhileIdle(
        AlarmManager.RTC_WAKEUP,
        reminderTime.timeInMillis,
        pendingIntent
    )
}

fun cancelDailyNotification(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, PrayerNotificationReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        DAILY_NOTIFICATION_REQUEST_CODE,
        intent,
        PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
    )
    if (pendingIntent != null) {
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}

fun cancelPrayerReminders(context: Context) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")

    for (prayerName in prayers) {
        val intent = Intent(context, PrayerNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            prayerName.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
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
    // Schedule Daily Notification
    val isDailyEnabled = dataStoreManager.isDailyNotificationEnabled.first()
    if (isDailyEnabled) {
        val time = dataStoreManager.notificationTime.first()
        val (hour, minute) = time.split(":").map { it.toInt() }
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        scheduleDailyNotification(context, calendar)
        Log.d("NotificationUtils", "Daily notification rescheduled for ${calendar.time}")
    }

    // Ensure prayer times are synced for the current year
    if (!prayerTimesRepository.isPrayerTimesSynced()) {
        val latitude = 36.402482
        val longitude = 3.323412
        val year = Calendar.getInstance().get(Calendar.YEAR)
        try {
            prayerTimesRepository.fetchAndStorePrayerTimes(latitude, longitude, year)
            Log.d("NotificationUtils", "Prayer times synced automatically")
        } catch (e: Exception) {
            Log.e("NotificationUtils", "Error syncing prayer times automatically: ${e.message}")
        }
    }

    // Schedule Prayer Reminders
    val isPrayerEnabled = dataStoreManager.isPrayerReminderEnabled.first()
    if (isPrayerEnabled) {
        val delay = dataStoreManager.prayerReminderDelay.first().toIntOrNull() ?: 15

        // Schedule for today and tomorrow to be safe
        val datesToSchedule = listOf(
            Calendar.getInstance(),
            Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) }
        )

        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        // Note: We don't cancel all prayer reminders here because we are scheduling for multiple days
        // and using the prayerName.hashCode() as requestCode which only allows one alarm per prayer name.
        // Actually, for multiple days, we need different request codes if we want multiple alarms for the same prayer.
        // But the user might only need the NEXT one.

        for (dateCalendar in datesToSchedule) {
            val formattedDate = dateFormat.format(dateCalendar.time)
            val prayerTimes = prayerTimesRepository.getPrayerTimesForDate(formattedDate)

            for (prayerTime in prayerTimes) {
                val calendar = Calendar.getInstance().apply {
                    time = dateCalendar.time
                    val timeStr = prayerTime.time.split(" ")[0]
                    val timeParts = timeStr.split(":")
                    if (timeParts.size == 2) {
                        set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
                        set(Calendar.MINUTE, timeParts[1].toInt())
                        set(Calendar.SECOND, 0)
                    }
                }

                // Only schedule if the reminder time is in the future
                val reminderTime = calendar.clone() as Calendar
                reminderTime.add(Calendar.MINUTE, delay)

                if (reminderTime.after(Calendar.getInstance())) {
                    // Use a unique request code for each prayer AND date to allow scheduling for multiple days
                    val requestCode = "${prayerTime.prayerName}_${formattedDate}".hashCode()

                    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(context, PrayerNotificationReceiver::class.java).apply {
                        putExtra("notification_type", "prayer_reminder")
                        putExtra("prayer_name", prayerTime.prayerName)
                    }

                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        if (alarmManager.canScheduleExactAlarms()) {
                             alarmManager.setExactAndAllowWhileIdle(
                                AlarmManager.RTC_WAKEUP,
                                reminderTime.timeInMillis,
                                pendingIntent
                            )
                            Log.d("NotificationUtils", "Prayer reminder scheduled for ${prayerTime.prayerName} at ${reminderTime.time}")
                        }
                    } else {
                         alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            reminderTime.timeInMillis,
                            pendingIntent
                        )
                         Log.d("NotificationUtils", "Prayer reminder scheduled for ${prayerTime.prayerName} at ${reminderTime.time}")
                    }
                }
            }
        }
    }
}
