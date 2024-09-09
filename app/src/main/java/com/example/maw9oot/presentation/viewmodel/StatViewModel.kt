package com.example.maw9oot.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.model.PrayerLog
import com.example.maw9oot.data.repository.PrayerLogRepository
import com.example.maw9oot.presentation.ui.enums.Prayer
import com.example.maw9oot.presentation.ui.enums.PrayerStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

@HiltViewModel
class StatViewModel @Inject constructor(
    private val repository: PrayerLogRepository
) : ViewModel() {

    private val _currentMonth = MutableLiveData(LocalDate.now().monthValue)
    val currentMonth: LiveData<Int> = _currentMonth

    private val _currentYear = MutableLiveData(LocalDate.now().year)
    val currentYear: LiveData<Int> = _currentYear

    private val _days = MutableLiveData<List<List<PrayerLog>>>(emptyList())
    val days: LiveData<List<List<PrayerLog>>> = _days

    private val _currentDayPrayers = MutableStateFlow<Map<Prayer, Boolean>>(emptyMap())
    val currentDayPrayers: StateFlow<Map<Prayer, Boolean>> = _currentDayPrayers

    private val _weeklyFajrChallenge = MutableStateFlow<Map<String, PrayerStatus>>(emptyMap())
    val weeklyFajrChallenge: StateFlow<Map<String, PrayerStatus>> = _weeklyFajrChallenge

    init {
        refreshData()
    }

    // Grouped data-fetching functions
    private fun refreshData() {
        fetchPrayerLogsForCurrentMonthYear()
        fetchPrayersForCurrentDay()
        fetchWeeklyFajrStatus()
    }

    private fun fetchPrayerLogsForCurrentMonthYear() {
        val month = _currentMonth.value ?: LocalDate.now().monthValue
        val year = _currentYear.value ?: LocalDate.now().year

        viewModelScope.launch {
            repository.getPrayersForMonthYear(month, year).collect { logs ->
                _days.value = preprocessLogs(logs, month, year)
            }
        }
    }

    private fun preprocessLogs(logs: List<PrayerLog>, month: Int, year: Int): List<List<PrayerLog>> {
        val daysInMonth = LocalDate.of(year, month, 1).lengthOfMonth()
        val allDays = List(daysInMonth) { mutableListOf<PrayerLog>() }

        logs.forEach { log ->
            try {
                val logDate = LocalDate.parse(log.date.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE)
                if (logDate.monthValue == month && logDate.year == year) {
                    val dayOfMonth = logDate.dayOfMonth - 1
                    if (dayOfMonth in allDays.indices) {
                        allDays[dayOfMonth].add(log)
                    } else {
                        Log.w("StatViewModel", "Date $logDate out of range for month $month")
                    }
                }
            } catch (e: DateTimeException) {
                Log.e("StatViewModel", "Error parsing date: ${log.date}", e)
            } catch (e: Exception) {
                Log.e("StatViewModel", "Unexpected error: ${e.message}", e)
            }
        }

        return allDays
    }

    private fun fetchPrayersForCurrentDay() {
        val currentDate = LocalDate.now().toString()

        viewModelScope.launch {
            try {
                val prayerStatuses = repository.getPrayerStatusesForDate(currentDate)
                _currentDayPrayers.value = prayerStatuses.map { (prayer, status) ->
                    prayer to (status == PrayerStatus.WITH_GROUP || status == PrayerStatus.ON_TIME_ALONE)
                }.toMap()
            } catch (e: Exception) {
                Log.e("StatViewModel", "Error fetching prayer statuses: ${e.message}", e)
            }
        }
    }

    private fun fetchWeeklyFajrStatus() {
        val today = LocalDate.now()
        val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

        viewModelScope.launch {
            try {
                val fajrStatusesForWeek = repository.getFajrStatusesForWeek(startOfWeek)
                _weeklyFajrChallenge.value = fajrStatusesForWeek
            } catch (e: Exception) {
                Log.e("StatViewModel", "Error fetching Fajr statuses: ${e.message}", e)
            }
        }
    }


    // Month/Year Update Handlers
    fun updateMonth(month: Int) {
        _currentMonth.value = month
        fetchPrayerLogsForCurrentMonthYear()
    }

    fun updateYear(year: Int) {
        _currentYear.value = year
        fetchPrayerLogsForCurrentMonthYear()
    }
}






