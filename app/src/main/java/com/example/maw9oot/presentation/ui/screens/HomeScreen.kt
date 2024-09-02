package com.example.maw9oot.presentation.ui.screens

import android.app.DatePickerDialog
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dokar.sheets.rememberBottomSheetState
import com.example.maw9oot.R
import com.example.maw9oot.presentation.ui.components.home.PrayerBottomSheet
import com.example.maw9oot.presentation.ui.components.home.PrayerButton
import com.example.maw9oot.presentation.ui.enums.Prayer
import com.example.maw9oot.presentation.ui.enums.PrayerStatus
import com.example.maw9oot.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState()

    val selectedPrayer by homeViewModel.selectedPrayer.observeAsState(Prayer.FAJR)
    val prayerStatuses by homeViewModel.prayerStatuses.observeAsState(emptyMap())
    val prayerTimes by homeViewModel.prayerTimeForDate.observeAsState(emptyMap())
    val selectedDate by homeViewModel.selectedDate.observeAsState("")

    val calendar = Calendar.getInstance()
    val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)

    fun showSheet(prayer: Prayer) {
        Log.d("HomeScreen", "prayerTimes: $prayerTimes")
        homeViewModel.selectPrayer(prayer)
        scope.launch { sheetState.expand(animate = true) }
    }

    fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            navController.context,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val newDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                homeViewModel.updateSelectedDate(newDate)
            },
            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Prevent future dates
        datePickerDialog.show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    val prevDate = Calendar.getInstance().apply {
                        time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedDate)!!
                        add(Calendar.DAY_OF_MONTH, -1)
                    }
                    homeViewModel.updateSelectedDate(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(prevDate.time))
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_navigate_before_24),
                    contentDescription = "Previous Day",
                    modifier = Modifier.size(35.dp)
                )
            }

            Text(
                text = selectedDate,
                modifier = Modifier.clickable { showDatePickerDialog() },
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(
                onClick = {
                    val nextDate = Calendar.getInstance().apply {
                        time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(selectedDate)!!
                        add(Calendar.DAY_OF_MONTH, 1)
                    }
                    val newDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(nextDate.time)
                    if (newDate <= currentDate) {
                        homeViewModel.updateSelectedDate(newDate)
                    }
                },
                enabled = selectedDate != currentDate
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_navigate_next_24),
                    contentDescription = "Next Day",
                    modifier = Modifier.size(35.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Prayer.entries.forEach { prayer ->
            PrayerButton(
                prayerName = prayer.prayerName,
                prayerIcon = { Image(painter = painterResource(id = prayer.icon), contentDescription = prayer.prayerName,modifier = Modifier.size(50.dp)) },
                prayerStatus = prayerStatuses[prayer] ?: PrayerStatus.NONE,
                prayerTime = prayerTimes[prayer] ?: "--:--",
            ) {
                showSheet(prayer)
            }
        }

        PrayerBottomSheet(
            prayer = selectedPrayer,
            sheetState = sheetState,
            currentStatus = prayerStatuses[selectedPrayer] ?: PrayerStatus.NONE,
            onStatusChange = { status ->
                val statusEnum = PrayerStatus.fromDisplayName(status)
                statusEnum.let {
                    homeViewModel.updateStatus(selectedPrayer, it, selectedDate)
                }
            }
        )
    }
}



