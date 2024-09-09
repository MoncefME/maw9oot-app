package com.example.maw9oot.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.maw9oot.data.local.PrayerDatabase
import com.example.maw9oot.data.model.PrayerLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.maw9oot.presentation.ui.enums.Prayer
import com.example.maw9oot.presentation.ui.enums.PrayerStatus
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
        // Define the week range (7 days starting from the startDate)
        val weekDates = (0..6).map { startDate.plusDays(it.toLong()) }

        // Convert dates to string format for querying the database
        val startDateString = startDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
        val endDateString = weekDates.last().format(DateTimeFormatter.ISO_LOCAL_DATE)

        // Get the prayer logs for the given week
        val fajrLogs = prayerDatabase.prayerLogDao().getFajrLogsForWeek(startDateString, endDateString).first()

        // Initialize the map with "NONE" for each day of the week
        val fajrStatusesForWeek = weekDates.associate { date ->
            date.dayOfWeek.name to PrayerStatus.NONE
        }.toMutableMap()

        // Populate the map with actual prayer statuses from the logs
        fajrLogs.forEach { log ->
            val logDate = LocalDate.parse(log.date, DateTimeFormatter.ISO_LOCAL_DATE)
            fajrStatusesForWeek[logDate.dayOfWeek.name] = PrayerStatus.valueOf(log.status)
        }

        return fajrStatusesForWeek
    }


}
