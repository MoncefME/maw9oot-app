package com.example.maw9oot.data.enums

import androidx.compose.ui.graphics.Color
import com.example.maw9oot.R

enum class Prayer(val prayerName: String, val icon: Int, val color: Color) {
    FAJR("Fajr", R.drawable.fajr, Color(0xFFd3f8e2)),
    DHUHR("Dhuhr", R.drawable.dhuhr, Color(0xFFe4c1f9)),
    ASR("Asr", R.drawable.asr, Color(0xFFf694c1)),
    MAGHRIB("Maghrib", R.drawable.maghrib, Color(0xFFede7b1)),
    ISHA("Isha", R.drawable.isha, Color(0xFFa9def9));

    companion object {
        fun fromName(prayerName: String): Prayer {
            return entries.find { it.prayerName == prayerName } ?: FAJR
        }
    }
}
