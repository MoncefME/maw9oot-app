package com.example.maw9oot.presentation.ui.components.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maw9oot.R


data class DailyPrayerStatus(
    val prayerName: String,
    val isCompletedOnTime: Boolean
)

data class WeeklyFajrStatus(
    val day: String,
    val isCompletedOnTime: Boolean
)

@Composable
fun ChallengesList() {
    val listState = remember { LazyListState() }
    val flingBehavior = rememberSnapFlingBehavior(listState)

    var selectedPage by remember { mutableIntStateOf(0) }
    val totalPages = 2

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Image(
                    painter = painterResource(id = R.drawable.challenge_icon),
                    modifier = Modifier.size(30.dp),
                    contentDescription = "Dark Theme Icon"
                )
                Text(
                    text = "Challenges",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Infos",
                modifier = Modifier.clickable {
                    showDialog = true
                }
            )
        }

        if (showDialog) {
            ChallengesInfosDialog(onDismiss = { showDialog = false })
        }

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ){
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                flingBehavior = flingBehavior,
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                items(totalPages) { page ->
                    if (page == 0) {
                        Card(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(150.dp)
                                .padding(horizontal = 2.dp)
                        ) {
                            DailyPrayerChallengeCard()
                        }
                    } else if (page == 1) {
                        Card(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(150.dp)
                                .padding(horizontal = 2.dp)
                        ) {
                            WeeklyFajrChallenge()
                        }
                    }
                }
            }


            DotsIndicator(
                totalDots = totalPages,
                selectedIndex = selectedPage,
                selectedColor = Color(0xFFFC8403),
                unSelectedColor = Color(0xFFE6E6E6)
            )
        }
    }


    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }.collect { index ->
            selectedPage = index
        }
    }
}

@Composable
fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color,
    unSelectedColor: Color
) {
    LazyRow(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(totalDots) { index ->
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width(30.dp)
                    .clip(CircleShape)
                    .background(if (index == selectedIndex) selectedColor else unSelectedColor)
            )
        }
    }
}

@Composable
fun DailyPrayerChallengeCard() {
    val dailyPrayerStatuses = listOf(
        DailyPrayerStatus("Fajr", true),
        DailyPrayerStatus("Dhuhr", false),
        DailyPrayerStatus("Asr", true),
        DailyPrayerStatus("Maghrib", true),
        DailyPrayerStatus("Isha", true)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Daily Prayer Challenge",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Complete All Prayers On Time",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            dailyPrayerStatuses.forEach { status ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(1.dp, Color.Gray, CircleShape)
                            .size(30.dp)
                            .background(if (status.isCompletedOnTime) Color(0xFF13b601) else Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (status.isCompletedOnTime) Icons.Filled.Check else Icons.Filled.Close,
                            contentDescription = "Status"
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = status.prayerName,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun WeeklyFajrChallenge() {
    val weeklyFajrStatuses = listOf(
        WeeklyFajrStatus("Mo", true),
        WeeklyFajrStatus("Tu", false),
        WeeklyFajrStatus("We", true),
        WeeklyFajrStatus("Th", true),
        WeeklyFajrStatus("Fr", true),
        WeeklyFajrStatus("Sa", false),
        WeeklyFajrStatus("Su", true)
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Weekly Fajr Challenge",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Complete Fajr Prayer On Time",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            weeklyFajrStatuses.forEach { status ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(30.dp)
                            .background(if (status.isCompletedOnTime) Color(0xFF13b601) else Color.Gray)
                            .border(1.dp, Color.Black, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (status.isCompletedOnTime) Icons.Filled.Check else Icons.Filled.Close,
                            contentDescription = "Status"
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = status.day,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}
