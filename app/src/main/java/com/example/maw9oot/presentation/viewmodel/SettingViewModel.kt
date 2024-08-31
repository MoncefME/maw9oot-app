package com.example.maw9oot.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.local.DataStoreManager
import com.example.maw9oot.data.repository.PrayerLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val isDarkTheme = dataStoreManager.isDarkTheme

    val language = dataStoreManager.language

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setDarkTheme(isDark)
        }
    }

    fun setLanguage(language: String){
        viewModelScope.launch {
            dataStoreManager.setLanguage(language)
        }
    }
}