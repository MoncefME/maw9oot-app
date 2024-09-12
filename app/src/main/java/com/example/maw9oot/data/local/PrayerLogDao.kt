package com.example.maw9oot.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.maw9oot.data.model.PrayerLog
import kotlinx.coroutines.flow.Flow

data class PrayerStatusPercentages(
    val withGroupPercentage: Double,
    val lateAlonePercentage: Double,
    val ontimeAlonePercentage: Double,
    val missedPercentage: Double
)

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

    @Query("SELECT * FROM prayer_log ORDER BY date")
    fun getPrayersBetweenDates(): Flow<List<PrayerLog>>

    @Query("SELECT * FROM prayer_log WHERE strftime('%m', date) = :month AND strftime('%Y', date) = :year")
    fun getPrayersForMonthYear(month: String, year: String): Flow<List<PrayerLog>>

    @Query("SELECT * FROM prayer_log WHERE date BETWEEN :startDate AND :endDate AND prayerType = :fajrType")
    fun getFajrLogsForWeek(startDate: String, endDate: String, fajrType: String = "Fajr"): Flow<List<PrayerLog>>


    @Query("SELECT COUNT(*) FROM prayer_log ")
    fun getTotalPrayerCount() : Flow<Int>

    @Query("SELECT COUNT(*) FROM prayer_log WHERE status= 'WITH_GROUP' ")
    fun getGroupPrayerCount(): Flow<Int>



    @Query("""
        SELECT COUNT(*) = 5
        FROM prayer_log
        WHERE date = :date
        AND status IN ('WITH_GROUP', 'ON_TIME')
    """)
    suspend fun isDayValid(date: String): Boolean

    @Query("""
    SELECT COUNT(T1.date) 
    FROM prayer_log AS T1 
    INNER JOIN prayer_log AS T2 
    ON T1.date = DATE(T2.date, '-1 day') 
    WHERE T1.status IN ('WITH_GROUP', 'ON_TIME_ALONE') 
    AND T2.status IN ('WITH_GROUP', 'ON_TIME_ALONE') 
    AND T1.date <= :today 
    GROUP BY T1.date 
    HAVING COUNT(DISTINCT T1.prayerType) = 5
""")
    suspend fun getCurrentStreak(today: String): Int

    @Query("""
    SELECT DISTINCT date
    FROM prayer_log
    WHERE status IN ('WITH_GROUP', 'ON_TIME_ALONE')
    AND date <= :today
    GROUP BY date
    HAVING COUNT(DISTINCT prayerType) = 5
    ORDER BY date DESC
""")
    suspend fun getValidDatesBefore(today:String): List<String>

    @Query("""
        SELECT COUNT(*)
        FROM (
            SELECT date
            FROM prayer_log
            WHERE status IN ('WITH_GROUP', 'ON_TIME_ALONE')
            GROUP BY date
            HAVING COUNT(DISTINCT prayerType) = 5
        )
    """)
    suspend fun getValidDates() : Int

}
