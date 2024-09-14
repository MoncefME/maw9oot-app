package com.example.maw9oot.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    //** Dark Theme **//
    private val darkThemeKey = booleanPreferencesKey("dark_theme")

    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[darkThemeKey] ?: false
        }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[darkThemeKey] = isDarkTheme
        }
    }

    //** Language **//
    private val languageKey = stringPreferencesKey("language")

    val language: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[languageKey] ?: "en"
        }

    suspend fun setLanguage(language: String) {
        context.dataStore.edit { preferences ->
            preferences[languageKey] = language
        }
    }

    //** Notification Time **//
    private val notificationTimeKey = stringPreferencesKey("notification_time")

    val notificationTime: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[notificationTimeKey] ?: "00:00"
    }

    suspend fun setNotificationTime(time: String) {
        context.dataStore.edit { preferences ->
            preferences[notificationTimeKey] = time
        }
    }

    //** Daily Notification **//
    private val dailyNotificationKey = booleanPreferencesKey("daily_notification")

    val isDailyNotificationEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[dailyNotificationKey] ?: false
        }

    suspend fun setDailyNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[dailyNotificationKey] = enabled
        }
    }

    //** Prayer Reminder **//
    private val prayerReminderKey = booleanPreferencesKey("prayer_reminder")

    val isPrayerReminderEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[prayerReminderKey] ?: false
        }

    suspend fun setPrayerReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[prayerReminderKey] = enabled
        }
    }

    //** Prayer Reminder Delay **//
    private val prayerReminderDelayKey = stringPreferencesKey("prayer_reminder_delay")

    val prayerReminderDelay: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[prayerReminderDelayKey] ?: "15"
        }

    suspend fun setPrayerReminderDelay(minutes: String) {
        context.dataStore.edit { preferences ->
            preferences[prayerReminderDelayKey] = minutes
        }
    }

    //** Security **//
    private val securityEnabledKey = booleanPreferencesKey("security_enabled")

    val isSecurityEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[securityEnabledKey] ?: false
        }

    suspend fun setSecurityEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[securityEnabledKey] = enabled
        }
    }

    //** Streak **//
    private val streakKey = intPreferencesKey("streak")

    val streak: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[streakKey] ?: 0 }

    suspend fun setStreak(streak: Int) {
        context.dataStore.edit { preferences -> preferences[streakKey] = streak }
    }

    //** Points **//
    private val pointsKey = intPreferencesKey("points")

    val points: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[pointsKey] ?: 0 }

    suspend fun setPoints(points: Int) {
        context.dataStore.edit { preferences -> preferences[pointsKey] = points }
    }

    //** Group Percentage **/
    private val groupPercentageKey = intPreferencesKey("group_percentage")

    val groupPercentage: Flow<Int> = context.dataStore.data
        .map { preferences -> preferences[groupPercentageKey] ?: 0 }

    suspend fun setGroupPercentage(percentage: Int) {
        context.dataStore.edit { preferences -> preferences[groupPercentageKey] = percentage }
    }
}