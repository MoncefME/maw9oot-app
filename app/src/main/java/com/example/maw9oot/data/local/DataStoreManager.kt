package com.example.maw9oot.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")

    private val LANGUAGE_KEY = stringPreferencesKey("language")

    private val NOTIFICATION_TIME_KEY = stringPreferencesKey("notification_time")

    private val DAILY_NOTIFICATION_KEY = booleanPreferencesKey("daily_notification")

    private val PRAYER_REMINDER_KEY = booleanPreferencesKey("prayer_reminder")

    private val PRAYER_REMINDER_DELAY_KEY = stringPreferencesKey("prayer_reminder_delay")

    private val SECURITY_ENABLED_KEY = booleanPreferencesKey("security_enabled")

    val isDailyNotificationEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DAILY_NOTIFICATION_KEY] ?: false
        }

    val isPrayerReminderEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PRAYER_REMINDER_KEY] ?: false
        }

    val prayerReminderDelay: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[PRAYER_REMINDER_DELAY_KEY] ?: "15" // Default to 15 minutes
        }


    val isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_THEME_KEY] ?: false
        }

    fun getInitialDarkTheme(): Boolean {
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[DARK_THEME_KEY] ?: false
            }.first()
        }
    }

    fun getInitialSecurity():Boolean{
        return runBlocking {
            context.dataStore.data.map { preferences ->
                preferences[SECURITY_ENABLED_KEY] ?: false
            }.first()
        }
    }
    val language : Flow<String> = context.dataStore.data
        .map{ preferences->
            preferences[LANGUAGE_KEY] ?: "en"
        }

    val notificationTime: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATION_TIME_KEY] ?: "00:00"
    }

    val isSecurityEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[SECURITY_ENABLED_KEY] ?: false
        }

    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDarkTheme
        }
    }

    suspend fun setLanguage(language: String){
        context.dataStore.edit{ preferences ->
            preferences[LANGUAGE_KEY] = language
        }
    }

    suspend fun setNotificationTime(time: String) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_TIME_KEY] = time
        }
    }

    suspend fun setDailyNotificationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DAILY_NOTIFICATION_KEY] = enabled
        }
    }

    suspend fun setPrayerReminderEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PRAYER_REMINDER_KEY] = enabled
        }
    }

    suspend fun setPrayerReminderDelay(minutes: String) {
        context.dataStore.edit { preferences ->
            preferences[PRAYER_REMINDER_DELAY_KEY] = minutes
        }
    }

    suspend fun setSecurityEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SECURITY_ENABLED_KEY] = enabled
        }
    }
}