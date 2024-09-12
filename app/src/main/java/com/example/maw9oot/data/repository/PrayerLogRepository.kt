package com.example.maw9oot.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.maw9oot.data.local.PrayerDatabase
import com.example.maw9oot.data.model.PrayerLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.maw9oot.data.enums.Prayer
import com.example.maw9oot.data.enums.PrayerStatus
import com.example.maw9oot.data.local.DataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PrayerLogRepository @Inject constructor(
    private val prayerDatabase: PrayerDatabase
) {

    suspend fun upsertPrayerLog(date: String, prayerType: String, status: String) {
        val existingLog = prayerDatabase.prayerLogDao().getPrayerLog(date, prayerType)
        if (existingLog != null) {
            prayerDatabase.prayerLogDao().updatePrayerStatus(date, prayerType, status)
        } else {
            prayerDatabase.prayerLogDao().insertPrayerLog(
                PrayerLog(
                    date = date,
                    prayerType = prayerType,
                    status = status
                )
            )
        }
    }


    suspend fun getPrayerStatusesForDate(date: String): Map<Prayer, PrayerStatus> {
        val prayerLogs = prayerDatabase.prayerLogDao().getPrayerLogsByDate(date).first()
        return prayerLogs.associate { log ->
            Prayer.fromName(log.prayerType) to PrayerStatus.valueOf(log.status)
        }
    }

    fun getPrayersForMonthYear(month: Int, year: Int): Flow<List<PrayerLog>> {
        val formattedMonth = month.toString().padStart(2, '0')
        val formattedYear = year.toString()
        return prayerDatabase.prayerLogDao().getPrayersForMonthYear(formattedMonth, formattedYear)
    }

    suspend fun getFajrStatusesForWeek(startDate: LocalDate): Map<String, PrayerStatus> {
        val weekDates = (0..6).map { startDate.plusDays(it.toLong()) }

        val startDateString = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val endDateString = weekDates.last().format(DateTimeFormatter.ISO_LOCAL_DATE)

        val fajrLogs = prayerDatabase.prayerLogDao().getFajrLogsForWeek(startDateString, endDateString).first()

        val fajrStatusesForWeek = weekDates.associate { date ->
            date.dayOfWeek.name to PrayerStatus.NONE
        }.toMutableMap()

        fajrLogs.forEach { log ->
            val logDate = LocalDate.parse(log.date, DateTimeFormatter.ISO_LOCAL_DATE)
            fajrStatusesForWeek[logDate.dayOfWeek.name] = PrayerStatus.valueOf(log.status)
        }

        return fajrStatusesForWeek
    }

     fun getTotalPrayerCount(): Flow<Int> {
        return prayerDatabase.prayerLogDao().getTotalPrayerCount()
    }

    fun getGroupPrayerCount(): Flow<Int> {
        return prayerDatabase.prayerLogDao().getGroupPrayerCount()
    }

    suspend fun getCurrentStreak(today: String): Int {
        val validDates = prayerDatabase.prayerLogDao().getValidDatesBefore(today)
        return calculateStreak(validDates, today)
    }


    private fun calculateStreak(validDates: List<String>, today: String): Int {
        if (validDates.isEmpty()) return 0


        Log.d("PrayerLogRepository", "validDates: $validDates")

        // Convert today to LocalDate
        var currentDate = LocalDate.parse(today)

        Log.d("PrayerLogRepository", "currentDate: $currentDate")
        var streak = 0

        // Create a set of valid dates for quick lookup
        val validDateSet = validDates.map { LocalDate.parse(it) }.toSet()

        Log.d("PrayerLogRepository", "validDateSet: $validDateSet")

        // Iterate backwards from today
        while (validDateSet.contains(currentDate)) {
            streak++
            currentDate = currentDate.minusDays(1)
        }


        return streak
    }

}
