package com.example.maw9oot.presentation.ui.components.stats.prayerLogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.maw9oot.R
import com.example.maw9oot.data.model.PrayerLog
import com.example.maw9oot.data.enums.Prayer
import com.example.maw9oot.presentation.viewmodel.StatViewModel
import java.time.LocalDate
import java.time.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun PrayerGrid(
    statViewModel: StatViewModel= hiltViewModel()
) {
    val days by statViewModel.days.observeAsState(emptyList())
    val prayers = Prayer.entries.map { it.prayerName }

    val today = LocalDate.now()
    val currentMonth by statViewModel.currentMonth.observeAsState(today.monthValue)
    val currentYear by statViewModel.currentYear.observeAsState(today.year)

    val displayedDays = (1..LocalDate.of(currentYear, currentMonth, 1).lengthOfMonth()).map { day ->
        LocalDate.of(currentYear, currentMonth, day).toString()
    }

    val isMonthPickerVisible = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        TitleRow(
            isMonthPickerVisible = isMonthPickerVisible,
            statViewModel = statViewModel
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(245.dp),
        ) {
            PrayerNameColumn()

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                items(displayedDays) { date ->
                    val dayLogs = days.find { it.firstOrNull()?.date?.substring(0, 10) == date } ?: emptyList()
                    PrayerDayColumn(date, prayers, dayLogs)
                }
            }
        }
    }
}

fun getStatusColor(status: String): Color {
    return when (status) {
        "MISSED" -> Color(0xFFf44336)
        "LATE_ALONE" -> Color(0xFFfe7838)
        "WITH_GROUP" -> Color(0xFF13b601)
        "ON_TIME_ALONE" -> Color(0xFF1eb4eb)
        else -> Color.Gray
    }
}

@Composable
fun PrayerNameColumn() {
    val prayers: List<String> = listOf(
        stringResource(id = R.string.PRAYER_FAJR),
        stringResource(id = R.string.PRAYER_DHUHR),
        stringResource(id = R.string.PRAYER_ASR),
        stringResource(id = R.string.PRAYER_MAGHRIB),
        stringResource(id = R.string.PRAYER_ISHA)
    )
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(8.dp))
            .width(75.dp)
            .padding(end = 4.dp),
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(2.dp))
                .fillMaxWidth()
                .height(32.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.PRAYER),
                fontSize = 15.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        prayers.forEach { prayer ->
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .fillMaxWidth()
                    .height(32.dp),
            ) {
                Text(
                    text = prayer,
                    fontSize = 15.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun PrayerDayColumn(
    date: String,
    prayers: List<String>,
    dayLogs: List<PrayerLog>
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clip(RoundedCornerShape(8.dp))
            .width(32.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = date.substring(8),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 4.dp)
                .align(Alignment.CenterHorizontally)
        )

        prayers.forEach { prayer ->
            val prayerLog = dayLogs.find { it.prayerType == prayer }
            val status = prayerLog?.status ?: "None"
            Box(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(getStatusColor(status))
                    .aspectRatio(1f)
                    .width(20.dp)
            ) {}
        }
    }
}

@Composable
fun TitleRow(
    isMonthPickerVisible: MutableState<Boolean>,
    statViewModel: StatViewModel
) {
    val currentMonth by statViewModel.currentMonth.observeAsState(LocalDate.now().monthValue)
    val currentYear by statViewModel.currentYear.observeAsState(LocalDate.now().year)

    var currentMonthYear by remember { mutableStateOf("") }

    currentMonthYear = "${Month.of(currentMonth).getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase()} ${currentYear % 100}"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.prayer_log_icon),
                modifier = Modifier.size(30.dp),
                contentDescription = "Dark Theme Icon"
            )
            Text(
                text = stringResource(id = R.string.STATS_PRAYER_LOG),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Button(
            onClick = { isMonthPickerVisible.value = true },
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            modifier = Modifier.height(35.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0x888590ea),
                contentColor = Color.Black
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Text(
                    text = currentMonthYear,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                Icon(
                    imageVector = Icons.Filled.DateRange,
                    contentDescription = "Infos"
                )

            }
        }




        MonthPicker(
            visible = isMonthPickerVisible.value,
            currentMonth = currentMonth -1,
            currentYear = currentYear,
            confirmButtonClicked = { month, year ->
                statViewModel.updateMonth(month )
                statViewModel.updateYear(year)
                isMonthPickerVisible.value = false
            },
            cancelClicked = {
                isMonthPickerVisible.value = false
            }
        )
    }
}

