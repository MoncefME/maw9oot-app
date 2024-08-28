package com.example.maw9oot.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.maw9oot.data.model.PrayerLog

@Database(entities = [PrayerLog::class], version = 1, exportSchema = false)
abstract class PrayerLogDatabase  :RoomDatabase() {
    abstract fun prayerLogDao(): PrayerLogDao
}