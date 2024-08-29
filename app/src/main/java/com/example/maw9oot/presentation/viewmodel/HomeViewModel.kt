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
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: PrayerLogRepository
) : ViewModel() {

    private val _selectedPrayer = MutableLiveData<Prayer>(Prayer.FAJR)
    val selectedPrayer: LiveData<Prayer> = _selectedPrayer

    private val _prayerStatuses = MutableLiveData<Map<Prayer, PrayerStatus>>()
    val prayerStatuses: LiveData<Map<Prayer, PrayerStatus>> = _prayerStatuses

    private val _selectedDate = MutableLiveData<String>().apply {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        value = dateFormat.format(Calendar.getInstance().time)
    }
    val selectedDate: LiveData<String> = _selectedDate

    init {
        // Load prayer statuses when the ViewModel is first created
        loadPrayerStatusesForSelectedDate()
    }

    fun selectPrayer(prayer: Prayer) {
        _selectedPrayer.value = prayer
    }

    fun updateStatus(prayer: Prayer, status: PrayerStatus, date: String) {
        val currentStatuses = _prayerStatuses.value.orEmpty().toMutableMap()
        currentStatuses[prayer] = status
        _prayerStatuses.value = currentStatuses

        viewModelScope.launch {
            repository.upsertPrayerLog(
                date = date,
                prayerType = prayer.prayerName,
                status = status.name
            )
        }
    }

    fun updateSelectedDate(newDate: String) {
        _selectedDate.value = newDate
        loadPrayerStatusesForSelectedDate() // Load prayer statuses for the new date
    }

    private fun loadPrayerStatusesForSelectedDate() {
        val date = _selectedDate.value ?: return
        viewModelScope.launch {
            val statuses = repository.getPrayerStatusesForDate(date)
            _prayerStatuses.value = statuses
        }
    }
}