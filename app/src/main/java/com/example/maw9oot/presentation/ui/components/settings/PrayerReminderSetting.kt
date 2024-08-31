package com.example.maw9oot.presentation.ui.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maw9oot.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayerReminderSetting(
    isEnabled: Boolean,
    delay: String,
    onDelaySelected: (String) -> Unit,
    onToggle: (Boolean) -> Unit
) {
    var showDurationPickerDialog by remember { mutableStateOf(false) }
    val durationPickerState = rememberTimePickerState(
        initialHour = 0,
        initialMinute = delay.toIntOrNull()?.rem(60) ?: 15,
        is24Hour = true
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Icon(painter = painterResource(id = R.drawable.baseline_access_time_24), contentDescription ="Dark Theme Icon" )
            Text(text = "Prayer Notification")
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (isEnabled) {
                Button(
                    onClick = { showDurationPickerDialog = true },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
                ) {
                    Text(text = "$delay min", fontSize = 18.sp)
                }
            }
            Switch(checked = isEnabled, onCheckedChange = onToggle)
        }
    }

    if (showDurationPickerDialog) {
        TimePickerDialog(
            onDismiss = { showDurationPickerDialog = false },
            onConfirm = {
                val selectedHour = durationPickerState.hour
                val selectedMinute = durationPickerState.minute
                val totalMinutes = (selectedHour * 60) + selectedMinute
                onDelaySelected(totalMinutes.toString())
                showDurationPickerDialog = false
            }
        ) {
            TimeInput(state = durationPickerState)
        }
    }
}