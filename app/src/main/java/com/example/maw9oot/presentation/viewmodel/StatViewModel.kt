package com.example.maw9oot.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.model.PrayerLog
import com.example.maw9oot.data.repository.PrayerLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatViewModel @Inject constructor(
    private val repository: PrayerLogRepository
) : ViewModel() {

//    private val _prayers = MutableStateFlow<List<List<PrayerLog>>>(emptyList())
//    val prayers: StateFlow<List<List<PrayerLog>>> = _prayers
//
//    init {
//        viewModelScope.launch {
//            _prayers.value = repository.getPrayersForDays() // Example date range
//            Log.d("StatViewModel", "Prayers: ${_prayers.value}")
//        }
//    }

    val prayers: StateFlow<List<List<PrayerLog>>> = repository.getPrayersForDays()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

}

