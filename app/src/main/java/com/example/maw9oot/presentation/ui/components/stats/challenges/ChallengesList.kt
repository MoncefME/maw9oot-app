package com.example.maw9oot.presentation.ui.components.stats.challenges

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.maw9oot.R
import com.example.maw9oot.presentation.ui.enums.PrayerStatus
import com.example.maw9oot.presentation.viewmodel.StatViewModel
import java.text.SimpleDateFormat
import java.util.Locale

data class DailyPrayerStatus(
    val prayerName: String, val isCompletedOnTime: Boolean
)

data class WeeklyFajrStatus(
    val day: String, val isCompletedOnTime: Boolean
)

private val dayOfWeekAbbreviations = mapOf(
    "MONDAY" to "Mo",
    "TUESDAY" to "Tu",
    "WEDNESDAY" to "We",
    "THURSDAY" to "Th",
    "FRIDAY" to "Fr",
    "SATURDAY" to "Sa",
    "SUNDAY" to "Su"
)

@Composable
fun ChallengesList(
    statViewModel: StatViewModel = hiltViewModel()
) {
    val listState = remember { LazyListState() }
    val flingBehavior = rememberSnapFlingBehavior(listState)

    var selectedPage by remember { mutableIntStateOf(0) }
    val totalPages = 2

    var showDialog by remember { mutableStateOf(false) }

    val prayerStatuses by statViewModel.currentDayPrayers.collectAsState()
    val weeklyFajrPrayers by statViewModel.weeklyFajrChallenge.collectAsState()

    Log.d("ChallengesList", "Prayer statuses: $prayerStatuses")
    Log.d("ChallengesList", "Weekly Fajr Prayers: $weeklyFajrPrayers")


    val dailyPrayerStatuses = prayerStatuses.map {
        DailyPrayerStatus(
            prayerName = it.key.prayerName,
            isCompletedOnTime = it.value
        )
    }

    val weeklyFajrStatuses = weeklyFajrPrayers.map {
        WeeklyFajrStatus(
            day = dayOfWeekAbbreviations[it.key] ?: it.key,
            isCompletedOnTime = it.value == PrayerStatus.ON_TIME_ALONE || it.value == PrayerStatus.WITH_GROUP
        )
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TitleRow(onShowDialog = { showDialog = it })

        if (showDialog) {
            ChallengesInfosDialog(onDismiss = { showDialog = false })
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                flingBehavior = flingBehavior,
                state = listState,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(horizontal = 2.dp)
            ) {
                items(totalPages) { page ->
                    if (page == 0) {
                        Card(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(150.dp)
                                .padding(horizontal = 2.dp), colors = CardDefaults.cardColors(
                                containerColor = Color(0x77cf9ed2)
                            )
                        ) {
                            DailyPrayerChallenge(dailyPrayerStatuses)
                        }
                    } else if (page == 1) {
                        Card(
                            modifier = Modifier
                                .fillParentMaxWidth()
                                .height(150.dp)
                                .padding(horizontal = 2.dp), colors = CardDefaults.cardColors(
                                containerColor = Color(0x77f6ca85)
                            )

                        ) {
                            WeeklyFajrChallenge(weeklyFajrStatuses)
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
fun TitleRow(onShowDialog: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
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

        Button(
            onClick = { onShowDialog(true) },
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            modifier = Modifier.height(35.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent, contentColor = Color.Black
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Infos",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Infos",
                )
            }
        }
    }
}

