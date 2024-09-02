package com.example.maw9oot.presentation.ui.enums

import androidx.compose.ui.graphics.Color
import com.example.maw9oot.R

enum class PrayerStatus(val displayName: String, val icon: Int, val color: Color) {
    MISSED("Missed", R.drawable.missed, Color(0xFFf14143)),
    LATE_ALONE("Late Alone", R.drawable.late_alone, Color(0xFFFF5722)),
    WITH_GROUP("With Group", R.drawable.group, Color(0xFF13B601)),
    ON_TIME_ALONE("On Time Alone", R.drawable.ontime_alone, Color(0xFF1EB4EB)),
    NONE("None", R.drawable.none, Color(0xFFf2eeff));

    companion object {
        fun fromDisplayName(displayName: String): PrayerStatus {
            return entries.find { it.displayName == displayName } ?: NONE
        }
    }
}

