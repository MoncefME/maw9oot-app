package com.example.maw9oot.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.repository.PrayerLogRepository
import com.example.maw9oot.presentation.ui.enums.Prayer
import com.example.maw9oot.presentation.ui.enums.PrayerStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PrayerLogRepository
) : ViewModel() {

    private val _selectedPrayer = MutableLiveData<Prayer>(Prayer.FAJR)
    val selectedPrayer: LiveData<Prayer> = _selectedPrayer

    private val _prayerStatuses = MutableLiveData<Map<Prayer, PrayerStatus>>().apply {
        value = Prayer.entries.associateWith { PrayerStatus.NONE }
    }
    val prayerStatuses: LiveData<Map<Prayer, PrayerStatus>> = _prayerStatuses

    fun selectPrayer(prayer: Prayer) {
        _selectedPrayer.value = prayer
    }

    fun updateStatus(prayer: Prayer, status: PrayerStatus) {
        val currentStatuses = _prayerStatuses.value.orEmpty().toMutableMap()
        currentStatuses[prayer] = status
        _prayerStatuses.value = currentStatuses

        viewModelScope.launch {
            repository.upsertPrayerLog(
                date = "2021-09-01",
                prayerType = prayer.prayerName,
                status = status.name
            )
        }
    }
}

