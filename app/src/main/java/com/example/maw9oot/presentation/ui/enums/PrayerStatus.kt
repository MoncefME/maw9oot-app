package com.example.maw9oot.presentation.ui.enums

import com.example.maw9oot.R

enum class PrayerStatus(val displayName: String, val icon: Int) {
    MISSED("Missed", R.drawable.missed),
    LATE_ALONE("Late Alone", R.drawable.late_alone),
    WITH_GROUP("With Group", R.drawable.group),
    ON_TIME_ALONE("On Time Alone", R.drawable.ontime_alone),
    NONE("None", R.drawable.none);

    companion object {
        fun fromDisplayName(displayName: String): PrayerStatus {
            return entries.find { it.displayName == displayName } ?: NONE
        }
    }
}

