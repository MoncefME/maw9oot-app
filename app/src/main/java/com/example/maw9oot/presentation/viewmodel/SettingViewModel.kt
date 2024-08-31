package com.example.maw9oot.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.local.DataStoreManager
import com.example.maw9oot.presentation.ui.utils.cancelAllNotifications
import com.example.maw9oot.presentation.ui.utils.scheduleDailyNotification
import com.example.maw9oot.presentation.ui.utils.schedulePrayerReminder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    val isDarkTheme = dataStoreManager.isDarkTheme
    val language = dataStoreManager.language
    val notificationTime = dataStoreManager.notificationTime
    val isDailyNotificationEnabled = dataStoreManager.isDailyNotificationEnabled
    val isPrayerReminderEnabled = dataStoreManager.isPrayerReminderEnabled
    val prayerReminderDelay = dataStoreManager.prayerReminderDelay
    val isSecurityEnabled = dataStoreManager.isSecurityEnabled

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setDarkTheme(isDark)
        }
    }

    fun setLanguage(language: String) {
        viewModelScope.launch {
            dataStoreManager.setLanguage(language)
        }
    }

    fun setNotificationTime(time: String) {
        viewModelScope.launch {
            dataStoreManager.setNotificationTime(time)
            // You might want to schedule a notification for the given time here
        }
    }

    fun toggleDailyNotification(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setDailyNotificationEnabled(enabled)
            if (enabled) {
                scheduleDailyNotification(appContext)
            } else {
                cancelAllNotifications(appContext)
            }
        }
    }

    fun togglePrayerReminder(enabled: Boolean, delayMinutes: String) {
        viewModelScope.launch {
            dataStoreManager.setPrayerReminderEnabled(enabled)
            dataStoreManager.setPrayerReminderDelay(delayMinutes)
            if (enabled) {
                // Example prayer times; replace with actual fetched times
                val prayerTimes = listOf("05:00", "12:00", "15:00", "18:00", "20:00")
                val delay = delayMinutes.toIntOrNull() ?: 15
                for (time in prayerTimes) {
                    val (hour, minute) = time.split(":").map { it.toInt() }
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, hour)
                        set(Calendar.MINUTE, minute)
                        set(Calendar.SECOND, 0)
                    }
                    schedulePrayerReminder(appContext, calendar, delay)
                }
            } else {
                cancelAllNotifications(appContext)
            }
        }
    }

    fun setPrayerReminderDelay(delay: String) {
        viewModelScope.launch {
            dataStoreManager.setPrayerReminderDelay(delay)
        }
    }

    fun enableSecurity(enabled: Boolean){
        viewModelScope.launch {
            dataStoreManager.setSecurityEnabled(enabled)
        }
    }
}
