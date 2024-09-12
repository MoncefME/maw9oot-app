package com.example.maw9oot.presentation.ui.components.stats.scoarboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.maw9oot.R
import com.example.maw9oot.presentation.viewmodel.StatViewModel

@Composable
fun ScoreLeaderboard(
    statViewModel: StatViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }

    val currentStreak by statViewModel.currentStreak.collectAsState(initial = 0)
    val currentPoints by statViewModel.currentPoints.collectAsState(initial = 0)
    val currentGroupPercentage by statViewModel.currentGroupPercentage.collectAsState(initial = 0)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TitleRow(onShowDialog = { showDialog = it })

        if (showDialog) {
            ScoreInfosDialog(onDismiss = { showDialog = false })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)

        ){
            Card(
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
                    .padding(horizontal = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x88f29284)
                )

            ) {
                StreakCard(currentStreak = currentStreak)
            }
            Card(
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
                    .padding(horizontal = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x8876b1fb)
                )
            ) {
                ScoreCard(currentPoints = currentPoints)
            }

            Card(
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
                    .padding(horizontal = 2.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0x889be0a2)
                )
            ) {
                GroupPrayerCard(currentGroupPercentage = currentGroupPercentage)
            }
        }

    }

}

@Composable
fun TitleRow(onShowDialog: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(id = R.drawable.scoreboard_icon),
                modifier = Modifier.size(30.dp),
                contentDescription = "Dark Theme Icon"
            )
            Text(
                text = stringResource(id = R.string.STATS_SCOREBOARD),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Button(
            onClick = { onShowDialog(true) },
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
            modifier = Modifier.height(35.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black
            ),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ){

                Text(
                    text = stringResource(id = R.string.STATS_INFOS),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                )

                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Infos",
                )

            }
        }
    }
}