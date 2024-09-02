package com.example.maw9oot.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.repository.PrayerLogRepository
import com.example.maw9oot.data.repository.PrayerTimesRepository
import com.example.maw9oot.presentation.ui.enums.Prayer
import com.example.maw9oot.presentation.ui.enums.PrayerStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val prayerLogRepository: PrayerLogRepository,
    private val prayerTimesRepository: PrayerTimesRepository
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

    private val _prayerTimeForDate = MutableLiveData<Map<Prayer, String>>()
    val prayerTimeForDate: LiveData<Map<Prayer, String>> = _prayerTimeForDate

    init {
        loadPrayerStatusesForSelectedDate()
        getPrayerTimesForDate(_selectedDate.value ?: "")
    }

    fun selectPrayer(prayer: Prayer) {
        _selectedPrayer.value = prayer
    }

    fun updateStatus(prayer: Prayer, status: PrayerStatus, date: String) {
        val currentStatuses = _prayerStatuses.value.orEmpty().toMutableMap()
        currentStatuses[prayer] = status
        _prayerStatuses.value = currentStatuses

        viewModelScope.launch {
            prayerLogRepository.upsertPrayerLog(
                date = date,
                prayerType = prayer.prayerName,
                status = status.name
            )
        }
    }

    fun updateSelectedDate(newDate: String) {
        _selectedDate.value = newDate
        loadPrayerStatusesForSelectedDate()
        getPrayerTimesForDate(newDate)
    }

    private fun loadPrayerStatusesForSelectedDate() {
        val date = _selectedDate.value ?: return
        viewModelScope.launch {
            val statuses = prayerLogRepository.getPrayerStatusesForDate(date)
            _prayerStatuses.value = statuses
        }
    }

    private fun getPrayerTimesForDate(date: String) {
        viewModelScope.launch {
            val prayerTimes =
                prayerTimesRepository.getPrayerTimesForDate(convertToDbDateFormat(date))
            val prayerTimeMap =
                prayerTimes.associateBy({ Prayer.fromName(it.prayerName) }, { it.time })
            _prayerTimeForDate.value = prayerTimeMap
        }
    }

    private fun convertToDbDateFormat(appDate: String): String {
        val appFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dbFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = appFormat.parse(appDate) ?: return appDate
        return dbFormat.format(date)
    }

    private fun convertToAppDateFormat(dbDate: String): String {
        val dbFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val appFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dbFormat.parse(dbDate) ?: return dbDate
        return appFormat.format(date)
    }
}