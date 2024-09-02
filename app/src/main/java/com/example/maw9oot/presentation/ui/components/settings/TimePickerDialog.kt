package com.example.maw9oot.presentation.ui.components.settings

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.maw9oot.R

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text= stringResource(id = R.string.timepicker_dismiss))
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(text= stringResource(id = R.string.timepicker_ok))
            }
        },
        text = { content() }
    )
}
