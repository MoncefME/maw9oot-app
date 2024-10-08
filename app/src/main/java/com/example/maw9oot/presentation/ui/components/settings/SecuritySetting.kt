package com.example.maw9oot.presentation.ui.components.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.maw9oot.R

@Composable
fun SecuritySetting(
    isSecure: Boolean,
    onToggle: (Boolean) -> Unit
) {
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
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_fingerprint_24),
                contentDescription = "Dark Theme Icon"
            )
            Text(text = stringResource(id = R.string.setting_security))
        }
        Switch(checked = isSecure, onCheckedChange = onToggle)
    }
}
