package com.example.maw9oot.presentation.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.m3.BottomSheet
import com.example.maw9oot.data.enums.Prayer
import com.example.maw9oot.data.enums.PrayerStatus
import com.example.maw9oot.ui.theme.PoppinsFontFamily
import kotlinx.coroutines.launch

@Composable
fun PrayerBottomSheet(
    prayer: Prayer,
    sheetState: BottomSheetState,
    currentStatus: PrayerStatus,
    onStatusChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    var selectedStatus by remember { mutableStateOf(currentStatus) }


    BottomSheet(
        dragHandle = {}, state = sheetState, modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = prayer.icon),
                    contentDescription = prayer.prayerName,
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "${prayer.prayerName} Prayer",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val statuses = PrayerStatus.entries.filter { it != PrayerStatus.NONE }
            statuses.forEach { status ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(vertical = 10.dp)
                        .clickable {
                            selectedStatus = status
                            onStatusChange(status.displayName)
                            scope.launch { sheetState.collapse(animate = true) }
                        },
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentStatus == status, onClick = {
                            selectedStatus = status
                            onStatusChange(status.displayName)
                            scope.launch { sheetState.collapse(animate = true) }
                        }, colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.tertiary,
                            unselectedColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = status.displayName,
                            textAlign = TextAlign.Start,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 30.sp,
                        )
                        Image(
                            painter = painterResource(id = status.icon),
                            contentDescription = "Group",
                            modifier = Modifier.size(50.dp)
                        )
                    }
                }
            }
        }
    }
}
