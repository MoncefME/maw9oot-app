package com.example.maw9oot.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.maw9oot.presentation.ui.components.settings.DailyReminderSetting
import com.example.maw9oot.presentation.ui.components.settings.DarkThemeSetting
import com.example.maw9oot.presentation.ui.components.settings.LanguageSetting
import com.example.maw9oot.presentation.ui.components.settings.PrayerReminderSetting
import com.example.maw9oot.presentation.ui.components.settings.PrayerTimesSync
import com.example.maw9oot.presentation.ui.components.settings.SecuritySetting
import com.example.maw9oot.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState(initial = false)
    val language by settingsViewModel.language.collectAsState(initial = "en")
    val notificationTime by settingsViewModel.notificationTime.collectAsState(initial = "")
    val isDailyNotificationEnabled by settingsViewModel.isDailyNotificationEnabled.collectAsState(
        initial = false
    )
    val isPrayerReminderEnabled by settingsViewModel.isPrayerReminderEnabled.collectAsState(initial = false)
    val prayerReminderDelay by settingsViewModel.prayerReminderDelay.collectAsState(initial = "15")
    val isSecure by settingsViewModel.isSecurityEnabled.collectAsState(initial = false)
    val isPrayerTimesSynced by settingsViewModel.isPrayerTimesSynced.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        item {
            DarkThemeSetting(isDarkTheme) { settingsViewModel.toggleTheme(it) }
        }

        item {
            LanguageSetting(language) { isArabic ->
                settingsViewModel.setLanguage(if (isArabic) "ar" else "en")
            }
        }

        item {
            DailyReminderSetting(
                isDailyNotificationEnabled,
                notificationTime,
                onTimeSelected = { formattedTime ->
                    settingsViewModel.setNotificationTime(
                        formattedTime
                    )
                },
                onToggle = { settingsViewModel.toggleDailyNotification(it) }
            )
        }

        item {
            PrayerReminderSetting(
                isPrayerReminderEnabled,
                prayerReminderDelay,
                onDelaySelected = { formattedDelay ->
                    settingsViewModel.setPrayerReminderDelay(
                        formattedDelay
                    )
                },
                onToggle = { settingsViewModel.togglePrayerReminder(it, prayerReminderDelay) }
            )
        }

        item {
            SecuritySetting(isSecure = isSecure, onToggle = {
                settingsViewModel.enableSecurity(it)
            })
        }

        item {
            PrayerTimesSync(
                isSync = isPrayerTimesSynced,
                onClick = {
                    settingsViewModel.syncPrayerTimes()
                }
            )
        }
    }
}
