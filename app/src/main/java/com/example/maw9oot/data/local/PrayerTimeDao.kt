package com.example.maw9oot.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maw9oot.data.model.PrayerTime
import kotlinx.coroutines.flow.Flow


@Dao
interface PrayerTimeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrayerTimes(prayerTimes: List<PrayerTime>)

    @Query("SELECT * FROM prayer_time WHERE date = :date")
    suspend fun getPrayerTimesByDate(date: String): List<PrayerTime>

    @Query("SELECT * FROM prayer_time WHERE prayerName = :prayerName AND date = :date")
    suspend fun getPrayerTime(prayerName: String, date: String): PrayerTime?

    @Query("SELECT * FROM prayer_time")
    fun getAllPrayerTimes(): Flow<List<PrayerTime>>

    @Query("SELECT COUNT(*) > 0 FROM prayer_time")
    suspend fun hasPrayerTimes(): Boolean
}