package com.example.maw9oot.presentation.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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

data class ToggleButtonOption(
    val label: String,
    val iconRes: Int? = null
)

@Composable
fun ToggleButton(
    options: List<ToggleButtonOption>,
    type: SelectionType,
    modifier: Modifier = Modifier,
    onClick: (ToggleButtonOption) -> Unit
) {
    var selectedOption by remember { mutableStateOf(options.first()) }

    Row(
        modifier = modifier
            .background(Color.Gray, MaterialTheme.shapes.small)
            .padding(4.dp)
    ) {
        options.forEach { option ->
            ToggleButtonOptionView(
                option = option,
                isSelected = selectedOption == option,
                onClick = {
                    selectedOption = option
                    onClick(option)
                }
            )
            if (option != options.last()) {
                Spacer(modifier = Modifier.width(4.dp).background(Color.DarkGray))
            }
        }
    }
}

@Composable
fun ToggleButtonOptionView(
    option: ToggleButtonOption,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable (onClick = onClick)
            .background(if (isSelected) Color.Blue else Color.Transparent, MaterialTheme.shapes.small)
            .padding(8.dp),
        color = if (isSelected) Color.Blue else Color.Transparent,
        contentColor = if (isSelected) Color.White else Color.Black
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (option.iconRes != null) {
                Image(painterResource(id = option.iconRes), contentDescription = null)
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = option.label, fontSize = 16.sp)
        }
    }
}

enum class SelectionType {
    SINGLE,
    MULTIPLE
}