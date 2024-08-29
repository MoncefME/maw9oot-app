package com.example.maw9oot.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.maw9oot.data.model.PrayerLog
import com.example.maw9oot.presentation.ui.enums.Prayer
import com.example.maw9oot.presentation.ui.enums.PrayerStatus

@Composable
fun PrayerGrid(
    days: List<List<PrayerLog>>
) {
    // Define spacing variables
    val paddingSmall = 4.dp
    val paddingMedium = 8.dp
    val paddingLarge = 16.dp
    val iconSize = 30.dp
    val boxSize = 30.dp
    val fontSize = 16.sp

    val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
//    val days = generateRandomPrayerLogs(60)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(paddingMedium)
    ) {
        // Column for Prayer Icons
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .width(iconSize)
        ) {
            // Empty Box for alignment
            Spacer(modifier = Modifier
                .padding(vertical = paddingMedium)
                .height(iconSize)
                .clip(RoundedCornerShape(2.dp))
                .width(iconSize)
            )

            prayers.forEach { prayer ->
                Box(
                    modifier = Modifier
                        .padding(vertical = paddingSmall)
                        .clip(RoundedCornerShape(2.dp))
                        .aspectRatio(1f)
                        .width(iconSize)
                ) {
                    Image(
                        painter = painterResource(id = Prayer.entries.find { it.prayerName == prayer }?.icon!!),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White) // Optional, to ensure icon visibility
                    )
                }
            }
        }

        // LazyRow for Days
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = paddingLarge)
        ) {
            items(days) { day ->
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = paddingSmall)
                        .clip(RoundedCornerShape(8.dp))
                        .width(iconSize),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = day.first().date.substring(8),
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = paddingSmall)
                            .align(Alignment.CenterHorizontally)
                    )
                    prayers.forEach { prayer ->
                        val prayerLog = day.find { it.prayerType == prayer }
                        val status = prayerLog?.status ?: "Missed"
                        Box(
                            modifier = Modifier
                                .padding(vertical = paddingSmall)
                                .clip(RoundedCornerShape(8.dp))
                                .background(getStatusColor(status))
                                .aspectRatio(1f)
                                .width(boxSize)
                        ) {
                            // Empty Box for status color
                        }
                    }
                }
            }
        }
    }
}


fun getStatusColor(status: String): Color {
    return when (status) {
        "MISSED" -> Color(0xFFf14143)
        "LATE_ALONE" -> Color(0xFFfe7838)
        "WITH_GROUP" -> Color(0xFF13b601)
        "ON_TIME_ALONE" -> Color(0xFF1eb4eb)
        else -> Color.Gray
    }
}
