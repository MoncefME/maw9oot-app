package com.example.maw9oot.data.repository

import com.example.maw9oot.data.local.PrayerDatabase
import com.example.maw9oot.data.model.PrayerLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.example.maw9oot.presentation.ui.enums.Prayer
import com.example.maw9oot.presentation.ui.enums.PrayerStatus
import kotlinx.coroutines.flow.first

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

    fun getPrayersForDays(): Flow<List<List<PrayerLog>>> {
        return prayerDatabase.prayerLogDao().getPrayersBetweenDates()
            .map { prayers ->
                prayers.groupBy { it.date }.values.toList()
            }
    }

    suspend fun getPrayerStatusesForDate(date: String): Map<Prayer, PrayerStatus> {
        val prayerLogs = prayerDatabase.prayerLogDao().getPrayerLogsByDate(date).first()
        return prayerLogs.associate { log ->
            Prayer.fromName(log.prayerType) to PrayerStatus.valueOf(log.status)
        }
    }
}
