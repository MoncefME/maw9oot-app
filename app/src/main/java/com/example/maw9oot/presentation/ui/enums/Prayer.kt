package com.example.maw9oot.presentation.ui.enums

import com.example.maw9oot.R

enum class Prayer(val prayerName: String, val icon: Int) {
    FAJR("Fajr", R.drawable.fajr),
    DHUHR("Dhuhr", R.drawable.dhuhr),
    ASR("Asr", R.drawable.asr),
    MAGHRIB("Maghrib", R.drawable.maghrib),
    ISHA("Isha", R.drawable.isha);

    companion object {
        fun fromName(prayerName: String): Prayer {
            return entries.find { it.prayerName == prayerName } ?: FAJR
        }
    }
}
