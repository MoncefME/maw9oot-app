package com.example.maw9oot.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.maw9oot.data.model.PrayerLog
import com.example.maw9oot.data.model.PrayerTime

@Database(
    entities = [PrayerLog::class, PrayerTime::class],
    version = 1,
    exportSchema = true,
)
abstract class PrayerDatabase : RoomDatabase() {
    abstract fun prayerLogDao(): PrayerLogDao
    abstract fun prayerTimeDao(): PrayerTimeDao
}
