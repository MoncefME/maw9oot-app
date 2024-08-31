package com.example.maw9oot.presentation.ui.components.stats

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maw9oot.presentation.ui.enums.Prayer

@Composable
fun PrayerStatsCard() {
    val statusPercentages = mapOf(
        "Alone" to 25,
        "Group" to 30,
        "Late" to 35,
        "Missed" to 10
    )

    val statusColors = mapOf(
        "Alone" to Color(0xFF1EB4EB),  // Blue
        "Group" to Color(0xFF13B601),  // Green
        "Late" to Color(0xFFFF5722),   // Deep Orange
        "Missed" to Color(0xFFf14143)   // Red
    )

    var mainPrayer by remember { mutableStateOf(Prayer.FAJR.prayerName) }

    val animatedCardColor by animateColorAsState(
        targetValue = Prayer.fromName(mainPrayer).color, label = ""
    )
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .animateContentSize()  // Animate size changes
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colors = CardDefaults.cardColors(containerColor = animatedCardColor),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "$mainPrayer in the last 30 days", style = MaterialTheme.typography.titleSmall.copy(color = Color.Black))
                StatusBar(statusPercentages, statusColors)
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Card for each prayer
            val prayers = listOf(Prayer.FAJR, Prayer.DHUHR, Prayer.ASR, Prayer.MAGHRIB, Prayer.ISHA)
            for (prayer in prayers) {
                val shape = when (prayer) {
                    Prayer.FAJR -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 16.dp,  // Radius only for bottom left corner
                        bottomEnd = 0.dp
                    )
                    Prayer.ISHA -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 16.dp  // Radius only for bottom right corner
                    )
                    else -> RoundedCornerShape(
                        topStart = 0.dp,
                        topEnd = 0.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp  // No radius for other cards
                    )
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp)
                        .clickable {
                            mainPrayer = prayer.prayerName
                        }
                        .animateContentSize(),
                    colors = CardDefaults.cardColors(containerColor = Prayer.fromName(prayer.prayerName).color),
                    shape = shape
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = prayer.icon),
                            contentDescription = prayer.prayerName,
                            modifier = Modifier.size(30.dp)
                        )
                        Text(text = prayer.prayerName, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBar(statusPercentages: Map<String, Int>, statusColors: Map<String, Color>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        statusPercentages.forEach { (status, percentage) ->
            Row(
                modifier = Modifier
                    .weight(percentage.toFloat())  // Use weight based on percentage
                    .fillMaxHeight()
                    .background(statusColors[status] ?: Color.Gray),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = status,
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )
                Text(
                    text = "$percentage%",
                    color = Color.White,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                )
            }
        }
    }
}
