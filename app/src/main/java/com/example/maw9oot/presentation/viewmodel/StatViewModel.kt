package com.example.maw9oot.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.model.PrayerLog
import com.example.maw9oot.data.repository.PrayerLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.DateTimeException
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter
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

    init {
        fetchPrayerLogsForCurrentMonthYear()
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

        // Create a list of empty lists for each day in the month
        val allDays = List(daysInMonth) { mutableListOf<PrayerLog>() }

        logs.forEach { log ->
            try {
                val logDate = LocalDate.parse(log.date.substring(0, 10), DateTimeFormatter.ofPattern("yyyy-MM-dd"))

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

    fun updateMonth(month: Int) {
        _currentMonth.value = month
        fetchPrayerLogsForCurrentMonthYear()
    }

    fun updateYear(year: Int) {
        _currentYear.value = year
        fetchPrayerLogsForCurrentMonthYear()
    }
}





