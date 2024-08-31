package com.example.maw9oot.presentation.ui.components.settings

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

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
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}
//fun TimePickerDialog(
//    title: String = "Select Time",
//    onDismiss: () -> Unit,
//    onConfirm: () -> Unit,
//    toggle: @Composable () -> Unit = {},
//    content: @Composable () -> Unit,
//) {
//    Dialog(
//        onDismissRequest = onDismiss,
//        properties = DialogProperties(usePlatformDefaultWidth = false),
//    ) {
//        Surface(
//            shape = MaterialTheme.shapes.extraLarge,
//            tonalElevation = 6.dp,
//            modifier =
//            Modifier
//                .width(IntrinsicSize.Min)
//                .height(IntrinsicSize.Min)
//                .background(
//                    shape = MaterialTheme.shapes.extraLarge,
//                    color = MaterialTheme.colorScheme.surface
//                ),
//        ) {
//            Column(
//                modifier = Modifier.padding(24.dp),
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(bottom = 20.dp),
//                    text = title,
//                    style = MaterialTheme.typography.labelMedium
//                )
//                content()
//                Row(
//                    modifier = Modifier
//                        .height(40.dp)
//                        .fillMaxWidth()
//                ) {
//                    toggle()
//                    Spacer(modifier = Modifier.weight(1f))
//                    TextButton(onClick = onDismiss) { Text("Cancel") }
//                    TextButton(onClick = onConfirm) { Text("OK") }
//                }
//            }
//        }
//    }
//}
