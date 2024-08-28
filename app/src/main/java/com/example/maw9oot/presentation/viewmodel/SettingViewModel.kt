package com.example.maw9oot.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.maw9oot.data.repository.PrayerLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: PrayerLogRepository
) : ViewModel()
{
}