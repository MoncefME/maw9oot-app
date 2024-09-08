package com.example.maw9oot.presentation.ui.components.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.shape.CircleShape

data class Challenge(
    val month: String,
    val prayer: String,
    val description: String,
    val currentStreak: Int,
    val maxStreak: Int
)

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
    val challenges = listOf(
        Challenge(
            prayer = "Fajr",
            month = "June",
            description = "Maintain a streak of Fajr prayers every day.",
            currentStreak = 15,
            maxStreak = 30,
        ),
        Challenge(
            prayer = "Dhuhr",
            month = "June",
            description = "Maintain a streak of Dhuhr prayers every day.",
            currentStreak = 10,
            maxStreak = 30
        ),
        Challenge(
            prayer = "Asr",
            month = "June",
            description = "Maintain a streak of Asr prayers every day.",
            currentStreak = 12,
            maxStreak = 30
        ),
        Challenge(
            prayer = "Maghrib",
            month = "June",
            description = "Maintain a streak of Maghrib prayers every day.",
            currentStreak = 25,
            maxStreak = 30
        ),
        Challenge(
            prayer = "Isha",
            month = "June",
            description = "Maintain a streak of Isha prayers every day.",
            currentStreak = 8,
            maxStreak = 30
        )
    )

    val listState = remember { LazyListState() }

    val flingBehavior = rememberSnapFlingBehavior(listState)

    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ){

            Text(
                text = "Challenges",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text="Badges"
            )
        }


        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            flingBehavior = flingBehavior,
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {

                Card(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .height(150.dp)
                        .padding(horizontal = 2.dp)
                ) {
                    DailyPrayerChallengeCard()

                }
            }

            item {
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
                            .clip(RoundedCornerShape(20.dp))
                            .border(
                                width = 1.dp,
                                color = Color.Gray,
                                shape = CircleShape
                            )
                            .size(24.dp)
                            .background(if (status.isCompletedOnTime) Color.Cyan else Color.Gray)

                    )
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
                            .clip(RoundedCornerShape(20.dp)) // Rounded circle
                            .size(24.dp) // Circle size
                            .background(if (status.isCompletedOnTime) Color.Green else Color.Red)
                            .border(
                                width = 1.dp,
                                color = Color.Black,
                                shape = CircleShape
                            ) // Border around circle

                    )
                    Spacer(modifier = Modifier.height(4.dp)) // Spacing between box and text
                    Text(
                        text = status.day,
                        fontSize = 12.sp, // Adjust text size for better readability
                        color = Color.Black
                    )
                }
            }
        }
    }
}
