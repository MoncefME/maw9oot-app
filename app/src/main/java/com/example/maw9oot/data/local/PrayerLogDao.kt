package com.example.maw9oot.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.maw9oot.data.model.PrayerLog
import kotlinx.coroutines.flow.Flow

@Dao
interface PrayerLogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerLog(prayerLog: PrayerLog)

    @Query("SELECT * FROM prayer_log WHERE date = :date AND prayerType = :prayerType")
    suspend fun getPrayerLog(date: String, prayerType: String): PrayerLog?

    @Query("UPDATE prayer_log SET status = :status WHERE date = :date AND prayerType = :prayerType")
    suspend fun updatePrayerStatus(date: String, prayerType: String, status: String)

    @Query("SELECT * FROM prayer_log WHERE date = :date")
    fun getPrayerLogsByDate(date: String): Flow<List<PrayerLog>>

    @Query("SELECT * FROM prayer_log")
    fun getAllPrayerLogs(): Flow<List<PrayerLog>>
}
