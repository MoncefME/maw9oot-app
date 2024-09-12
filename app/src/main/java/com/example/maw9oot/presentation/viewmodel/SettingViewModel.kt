package com.example.maw9oot.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.local.DataStoreManager
import com.example.maw9oot.data.utils.cancelAllNotifications
import com.example.maw9oot.data.utils.scheduleDailyNotification
import com.example.maw9oot.data.utils.schedulePrayerReminder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.maw9oot.data.repository.PrayerTimesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Locale

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val prayerTimesRepository: PrayerTimesRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    val isDarkTheme = dataStoreManager.isDarkTheme
    val language = dataStoreManager.language
    val isSecurityEnabled = dataStoreManager.isSecurityEnabled

    val notificationTime = dataStoreManager.notificationTime
    val isDailyNotificationEnabled = dataStoreManager.isDailyNotificationEnabled

    val isPrayerReminderEnabled = dataStoreManager.isPrayerReminderEnabled
    val prayerReminderDelay = dataStoreManager.prayerReminderDelay

    private val _isPrayerTimesSynced = MutableStateFlow(false)
    val isPrayerTimesSynced: StateFlow<Boolean> = _isPrayerTimesSynced

    init {
        viewModelScope.launch {
            _isPrayerTimesSynced.value = prayerTimesRepository.isPrayerTimesSynced()
        }
    }

    fun syncPrayerTimes() {
        val latitude = 36.402482
        val longitude = 3.323412
        val year: Int = Calendar.getInstance().get(Calendar.YEAR)
        viewModelScope.launch {
            try {
                // Only sync if not already synced
                if (!_isPrayerTimesSynced.value) {
                    prayerTimesRepository.fetchAndStorePrayerTimes(latitude, longitude, year)
                    _isPrayerTimesSynced.value = prayerTimesRepository.isPrayerTimesSynced()
                }
            } catch (e: Exception) {
                // Handle exceptions if needed (e.g., logging or displaying an error message)
                Log.e("SettingsViewModel", "Error syncing prayer times: ${e.message}")
                _isPrayerTimesSynced.value = false
            }
        }
    }

    // Theme
    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setDarkTheme(isDark)
        }
    }

    // Language
    fun setLanguage(language: String) {
        viewModelScope.launch {
            dataStoreManager.setLanguage(language)
            updateLocale(language, appContext)
            Log.d("SettingsViewModel", "Language set to $language")
        }
    }

    private fun updateLocale(language: String, context: Context) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }

    // Daily Notification [OK]
    fun setNotificationTime(time: String) {
        Log.d("SettingsViewModel", "Notification time set to $time")
        viewModelScope.launch {
            dataStoreManager.setNotificationTime(time)
            val (hour, minute) = time.split(":").map { it.toInt() }
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis() // Set the current time first
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
                set(Calendar.SECOND, 0)
                if (before(Calendar.getInstance())) {
                    // If the time is before the current time, schedule for the next day
                    add(Calendar.DAY_OF_YEAR, 1)
                }
            }
            // Cancel any previously set daily notifications
            cancelAllNotifications(appContext)
            // Schedule the new daily notification
            Log.d("SettingsViewModel", "Scheduling daily notification for $calendar")
            scheduleDailyNotification(appContext, calendar)
        }
    }

    fun toggleDailyNotification(enabled: Boolean) {
        Log.d("SettingsViewModel", "Daily notification enabled: $enabled")
        viewModelScope.launch {
            dataStoreManager.setDailyNotificationEnabled(enabled)
            if (enabled) {
                val time = dataStoreManager.notificationTime.first()
                Log.d("SettingsViewModel", "Daily notification time: $time")
                val (hour, minute) = time.split(":").map { it.toInt() }
                val calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, hour)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                    if (before(Calendar.getInstance())) {
                        add(Calendar.DAY_OF_YEAR, 1)
                    }
                }
                scheduleDailyNotification(appContext, calendar)
            } else {
                cancelAllNotifications(appContext)
            }
        }
    }

    // Prayer Reminder
    fun togglePrayerReminder(enabled: Boolean, delayMinutes: String) {
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        Log.d("SettingsViewModel", "Prayer reminder enabled: $enabled, delay: $delayMinutes")
        viewModelScope.launch {

            dataStoreManager.setPrayerReminderEnabled(enabled)
            dataStoreManager.setPrayerReminderDelay(delayMinutes)

            if (enabled) {
                val delay = delayMinutes.toIntOrNull() ?: 15
                val prayerTimes = prayerTimesRepository.getPrayerTimesForDate(formattedDate)

                Log.d("SettingsViewModel", "Prayer times for $formattedDate: $prayerTimes")

                if (prayerTimes.isNotEmpty()) {
                    for (prayerTime in prayerTimes) {
                        val calendar = Calendar.getInstance().apply {
                            val time = prayerTime.time.split(" ")[0]
                            val timeParts = time.split(":")
                            if (timeParts.size == 2) {
                                set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
                                set(Calendar.MINUTE, timeParts[1].toInt())
                                set(Calendar.SECOND, 0)
                            }
                        }
                        schedulePrayerReminder(appContext, calendar, delay, prayerTime.prayerName)
                    }
                } else {
                    Log.e("PrayerReminder", "No prayer times found for $formattedDate")
                }
            } else {
                cancelAllNotifications(appContext)
            }
        }
    }


    fun setPrayerReminderDelay(delay: String) {
        viewModelScope.launch {
            dataStoreManager.setPrayerReminderDelay(delay)
            Log.d("SettingsViewModel", "Prayer reminder delay set to $delay")
            val enabled = dataStoreManager.isPrayerReminderEnabled.first()
            togglePrayerReminder(enabled, delay)
        }
    }

    // Security
    fun enableSecurity(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setSecurityEnabled(enabled)
        }
    }
}
