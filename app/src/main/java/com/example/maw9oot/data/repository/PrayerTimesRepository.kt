package com.example.maw9oot.data.repository

import android.util.Log
import com.example.maw9oot.data.local.PrayerTimeDao
import com.example.maw9oot.data.model.PrayerTime
import com.example.maw9oot.data.remote.PrayerTimeAPI
import javax.inject.Inject

class PrayerTimesRepository @Inject constructor(
    private val prayerTimesApi: PrayerTimeAPI,
    private val prayerTimeDao: PrayerTimeDao
) {

    private val prayerNames = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")

    suspend fun fetchAndStorePrayerTimes(latitude: Double, longitude: Double, year: Int) {
        try {
            val response = prayerTimesApi.getPrayerTimes(year=year,latitude=latitude, longitude=longitude)
            val prayerDataList = response.data.flatMap { it.value }
            val prayersForDays = prayerDataList.map { prayerData ->
                prayerNames.map { prayerName ->
                    PrayerTime(
                        date = prayerData.date.gregorian.date,
                        prayerName = prayerName,
                        time = when (prayerName) {
                            "Fajr" -> prayerData.timings.Fajr
                            "Dhuhr" -> prayerData.timings.Dhuhr
                            "Asr" -> prayerData.timings.Asr
                            "Maghrib" -> prayerData.timings.Maghrib
                            "Isha" -> prayerData.timings.Isha
                            else -> ""
                        },
                        hijriDate = prayerData.date.hijri.date
                    )
                }
            }.flatten()
            if(prayersForDays.isNotEmpty()) {
                prayerTimeDao.insertPrayerTimes(prayersForDays)
                Log.d("PrayerTimesRepository", "fetchAndStorePrayerTimes Inserted to DB")
            }
            Log.d("PrayerTimesRepository", "fetchAndStorePrayerTimes: $prayersForDays")
        } catch (e: Exception) {
            Log.e("PrayerTimesRepository", "Error fetching prayer times", e)
        }
    }

    suspend fun getPrayerTimesForDate(date: String): List<PrayerTime> {
        return prayerTimeDao.getPrayerTimesByDate(date)
    }

    suspend fun isPrayerTimesSynced(): Boolean {
        return prayerTimeDao.hasPrayerTimes()
    }
}
