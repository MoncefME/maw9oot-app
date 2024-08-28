package com.example.maw9oot.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_log")
data class PrayerLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val prayerType : String,
    val status: String
)
