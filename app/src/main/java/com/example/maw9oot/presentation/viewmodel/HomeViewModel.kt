package com.example.maw9oot.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.maw9oot.data.repository.PrayerLogRepository
import com.example.maw9oot.data.repository.PrayerTimesRepository
import com.example.maw9oot.data.enums.Prayer
import com.example.maw9oot.data.enums.PrayerStatus
import com.example.maw9oot.data.local.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.*

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val prayerLogRepository: PrayerLogRepository,
    private val prayerTimesRepository: PrayerTimesRepository,
    private val dataStoreManager: DataStoreManager
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

            when (status) {
                PrayerStatus.WITH_GROUP -> updatePointsAndPercentage(2)
                PrayerStatus.ON_TIME_ALONE -> updatePointsAndPercentage(1)
                PrayerStatus.LATE_ALONE -> updatePointsAndPercentage(-2)
                PrayerStatus.MISSED -> updatePointsAndPercentage(-4)
                PrayerStatus.NONE -> TODO()
            }

            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().time)
            updateStreak(today)
        }
    }

    private suspend fun updatePointsAndPercentage(pointsDelta: Int) {
        viewModelScope.launch {

            val currentPoints = dataStoreManager.points.first()
            dataStoreManager.setPoints(currentPoints + pointsDelta)

            val totalPrayers = prayerLogRepository.getTotalPrayerCount().first()
            val groupPrayers = prayerLogRepository.getGroupPrayerCount().first()

            val groupPercentage = if (totalPrayers > 0) {
                (groupPrayers.toDouble() / totalPrayers * 100).toInt()
            } else {
                0
            }

            dataStoreManager.setGroupPercentage(groupPercentage)
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



    private fun updateStreak(today: String) {
        viewModelScope.launch {
            val streak = prayerLogRepository.getCurrentStreak(today)
            dataStoreManager.setStreak(streak)
        }
    }

}