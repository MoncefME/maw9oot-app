package com.example.maw9oot.presentation.ui.components.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import com.example.maw9oot.data.model.PrayerLog
import com.example.maw9oot.presentation.ui.enums.Prayer
import com.example.maw9oot.presentation.ui.utils.getMonthYear
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun PrayerGrid(
    days: List<List<PrayerLog>>
) {
    val paddingSmall = 4.dp
    val paddingMedium = 8.dp
    val iconSize = 32.dp
    val boxSize = 30.dp
    val fontSize = 18.sp


    val prayers = Prayer.entries.map { it.prayerName }

    var currentMonthYear by remember { mutableStateOf("") }

    val dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val today = LocalDate.now()
    val displayedDays = mutableListOf<String>()

    val startDate = days.firstOrNull()?.firstOrNull()?.date?.substring(0, 10) ?: today.toString()
    val endDate = days.lastOrNull()?.firstOrNull()?.date?.substring(0, 10) ?: today.toString()
    val start = LocalDate.parse(startDate, dateFormat)
    val end = LocalDate.parse(endDate, dateFormat)

    var currentDate = start
    while (currentDate <= end || displayedDays.size < 8) {
        displayedDays.add(currentDate.toString())
        currentDate = currentDate.plusDays(1)
    }

    val scrollState = rememberLazyListState()


    LaunchedEffect(scrollState.firstVisibleItemIndex) {
        val visibleStartDate = displayedDays.getOrNull(scrollState.firstVisibleItemIndex) ?: ""
        if (visibleStartDate.isNotEmpty()) {
            currentMonthYear = getMonthYear(visibleStartDate)
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(245.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .width(85.dp)
                .padding(end = paddingMedium),
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = paddingSmall)
                    .clip(RoundedCornerShape(2.dp))
                    .width(85.dp)
                    .height(40.dp)
                    .padding(end = paddingSmall, start = paddingMedium),
                contentAlignment = Alignment.BottomEnd,
            ) {
                Text(
                    text = currentMonthYear,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = paddingSmall, end = paddingSmall)
                )
            }

            prayers.forEach { prayer ->
                Box(
                    modifier = Modifier
                        .padding(vertical = paddingSmall)
                        .clip(RoundedCornerShape(2.dp))
                        .width(85.dp)
                        .height(iconSize)
                        .padding(start = paddingSmall)
                ) {
                    Text(
                        text = prayer,
                        fontSize = 15.sp,
                        modifier = Modifier.align(Alignment.TopEnd)
                    )
                }
            }
        }

        LazyRow(
            state = scrollState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingMedium),
        ) {
            items(displayedDays) { date ->
                val dayLogs =
                    days.find { it.firstOrNull()?.date?.substring(0, 10) == date } ?: emptyList()

                Column(
                    modifier = Modifier
                        .padding(horizontal = paddingSmall)
                        .clip(RoundedCornerShape(8.dp))
                        .width(iconSize),
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = date.substring(8),
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = paddingSmall)
                            .align(Alignment.CenterHorizontally)
                    )

                    prayers.forEach { prayer ->
                        val prayerLog = dayLogs.find { it.prayerType == prayer }
                        val status = prayerLog?.status ?: "Missed"
                        Box(
                            modifier = Modifier
                                .padding(vertical = paddingSmall)
                                .clip(RoundedCornerShape(8.dp))
                                .background(getStatusColor(status))
                                .aspectRatio(1f)
                                .width(boxSize)
                        ) {}
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


