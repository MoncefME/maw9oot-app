package com.example.maw9oot.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.model.PrayerLog
import com.example.maw9oot.data.repository.PrayerLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class StatViewModel @Inject constructor(
    private val repository: PrayerLogRepository
) : ViewModel() {

    val prayerLogs: StateFlow<List<PrayerLog>> = repository.getPrayerLogsByDate("2021-09-01")
        .map { it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
