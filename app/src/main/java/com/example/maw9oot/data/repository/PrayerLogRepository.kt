package com.example.maw9oot.data.repository

import com.example.maw9oot.data.local.PrayerLogDao
import com.example.maw9oot.data.model.PrayerLog
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PrayerLogRepository @Inject constructor(
    private val prayerLogDao: PrayerLogDao
) {

    suspend fun insertPrayerLog(prayerLog: PrayerLog) {
        prayerLogDao.insertPrayerLog(prayerLog)
    }

    fun getPrayerLogsByDate(date: String): Flow<List<PrayerLog>> {
        return prayerLogDao.getPrayerLogsByDate(date)
    }

    fun getAllPrayerLogs(): Flow<List<PrayerLog>> {
        return prayerLogDao.getAllPrayerLogs()
    }

    suspend fun upsertPrayerLog(date: String, prayerType: String, status: String) {
        val existingLog = prayerLogDao.getPrayerLog(date, prayerType)
        if (existingLog != null) {
            prayerLogDao.updatePrayerStatus(date, prayerType, status)
        } else {
            prayerLogDao.insertPrayerLog(
                PrayerLog(
                    date = date,
                    prayerType = prayerType,
                    status = status
                )
            )
        }
    }
}
