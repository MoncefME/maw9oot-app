package com.example.maw9oot.presentation.ui.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.maw9oot.R

@Composable
fun PrayerTimesSync(
    isSync: Boolean, onClick: () -> Unit
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
                painter = if (isSync) {
                    painterResource(id = R.drawable.baseline_download_done_24)
                } else {
                    painterResource(id = R.drawable.baseline_cloud_download_24)
                }, contentDescription = "Sync Icon", tint = if (isSync) {
                    Color(0xFF13B601)
                } else {
                    Color.Unspecified
                }
            )
            Text(text = stringResource(id = R.string.setting_sync_times))
        }

        Icon(
            painter = painterResource(id = R.drawable.baseline_download_24),

            contentDescription = "Sync Status Icon",
            modifier = Modifier
                .size(35.dp)
                .clickable { onClick() },

            )

    }
}
