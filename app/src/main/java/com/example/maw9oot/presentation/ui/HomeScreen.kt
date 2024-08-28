package com.example.maw9oot.presentation.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dokar.sheets.rememberBottomSheetState
import com.example.maw9oot.presentation.ui.components.PrayerBottomSheet
import com.example.maw9oot.presentation.ui.components.PrayerButton
import com.example.maw9oot.presentation.ui.enums.Prayer
import com.example.maw9oot.presentation.ui.enums.PrayerStatus
import com.example.maw9oot.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberBottomSheetState()

    val selectedPrayer by homeViewModel.selectedPrayer.observeAsState(Prayer.FAJR)
    val prayerStatuses by homeViewModel.prayerStatuses.observeAsState(Prayer.entries.associateWith { PrayerStatus.NONE })

    fun showSheet(prayer: Prayer) {
        homeViewModel.selectPrayer(prayer)
        scope.launch { sheetState.expand(animate = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Prayers", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Prayer.entries.forEach { prayer ->
            PrayerButton(
                prayerName = prayer.prayerName,
                prayerIcon = { Image(painter = painterResource(id = prayer.icon), contentDescription = prayer.prayerName) },
                prayerStatus = prayerStatuses[prayer] ?: PrayerStatus.NONE,
                onClick = { showSheet(prayer) }
            )
        }

        PrayerBottomSheet(
            prayer = selectedPrayer,
            sheetState = sheetState,
            currentStatus = prayerStatuses[selectedPrayer] ?: PrayerStatus.NONE,
            onStatusChange = { status ->
                val statusEnum = PrayerStatus.fromDisplayName(status)
                statusEnum.let { homeViewModel.updateStatus(selectedPrayer, it) }
            }
        )
    }
}
