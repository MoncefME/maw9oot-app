package com.example.maw9oot.presentation.viewmodel

import android.content.Context
import android.content.res.Configuration
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
import java.util.Locale
import javax.inject.Inject
import android.os.Build
import android.os.LocaleList
import android.app.LocaleManager
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.maw9oot.data.repository.PrayerTimesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val prayerTimesRepository: PrayerTimesRepository,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    val isDarkTheme = dataStoreManager.isDarkTheme
    val language = dataStoreManager.language
    val notificationTime = dataStoreManager.notificationTime
    val isDailyNotificationEnabled = dataStoreManager.isDailyNotificationEnabled
    val isPrayerReminderEnabled = dataStoreManager.isPrayerReminderEnabled
    val prayerReminderDelay = dataStoreManager.prayerReminderDelay
    val isSecurityEnabled = dataStoreManager.isSecurityEnabled

    private val _isPrayerTimesSynced = MutableStateFlow(false)
    val isPrayerTimesSynced: StateFlow<Boolean> = _isPrayerTimesSynced

    init {
        viewModelScope.launch {
            _isPrayerTimesSynced.value = prayerTimesRepository.isPrayerTimesSynced()
        }
    }

    fun syncPrayerTimes() {
        val latitude: Double = 36.402482
        val longitude: Double = 3.323412
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


    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setDarkTheme(isDark)
        }
    }

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

    fun fetchPrayerTimes(latitude: Double, longitude: Double, year: Int){
        viewModelScope.launch {
            prayerTimesRepository.fetchAndStorePrayerTimes(latitude, longitude, year)
        }
    }
}
