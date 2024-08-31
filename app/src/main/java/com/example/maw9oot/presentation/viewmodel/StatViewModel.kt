package com.example.maw9oot.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.model.PrayerLog
import com.example.maw9oot.data.repository.PrayerLogRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class StatViewModel @Inject constructor(
    private val repository: PrayerLogRepository
) : ViewModel() {

    private val _days = MutableStateFlow<List<List<PrayerLog>>>(emptyList())
    val days: StateFlow<List<List<PrayerLog>>> = _days

    init {
        viewModelScope.launch {
            repository.getPrayersForDays()
                .map { logs -> preprocessLogs(logs) }
                .collect { processedLogs -> _days.value = processedLogs }
        }
    }
    private fun preprocessLogs(logs: List<List<PrayerLog>>): List<List<PrayerLog>> {
        if (logs.isEmpty()) return List(8) { emptyList() }

        val allDays = mutableListOf<List<PrayerLog>>()
        val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")


        val startDate = logs.firstOrNull()?.firstOrNull()?.date?.substring(0, 10) ?: LocalDate.now().toString()
        val endDate = logs.lastOrNull()?.firstOrNull()?.date?.substring(0, 10) ?: LocalDate.now().toString()
        val start = LocalDate.parse(startDate, dateFormat)
        val end = LocalDate.parse(endDate, dateFormat)


        var currentDate = start
        while (currentDate <= end) {
            val dayLogs = logs.find { it.firstOrNull()?.date?.substring(0, 10) == currentDate.toString() } ?: emptyList()
            allDays.add(dayLogs)
            currentDate = currentDate.plusDays(1)
        }

        if (allDays.size < 8) {
            val paddingDays = 8 - allDays.size
            repeat(paddingDays) {
                allDays.add(emptyList())
                end.plusDays(1)
            }
        }

        return allDays
    }


}

