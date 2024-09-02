package com.example.maw9oot.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "prayer_time")
data class PrayerTime (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val prayerName: String,
    val date : String,
    val hijriDate:  String,
    val time: String
)