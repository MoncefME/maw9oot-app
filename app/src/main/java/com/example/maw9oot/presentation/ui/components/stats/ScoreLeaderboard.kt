package com.example.maw9oot.presentation.ui.components.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.maw9oot.R

@Composable
fun ScoreLeaderboard() {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                Image(
                    painter = painterResource(id = R.drawable.scoreboard_icon),
                    modifier = Modifier.size(30.dp),
                    contentDescription = "Dark Theme Icon"
                )
                Text(
                    text = "Scoreboard",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = "Infos",
                modifier = Modifier.clickable {
                    showDialog = true
                }
            )
        }

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
                    .padding(horizontal = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(id = R.drawable.streak),
                        modifier = Modifier.size(30.dp),
                        contentDescription = "Dark Theme Icon"
                    )
                    Text(
                        text="Streak",
                        fontSize = 12.sp
                    )
                    Text(
                        text="20 day",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Card(
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
                    .padding(horizontal = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(id = R.drawable.score),
                        modifier = Modifier.size(30.dp),
                        contentDescription = "Dark Theme Icon"
                    )
                    Text(
                        text="Score",
                        fontSize = 12.sp
                    )
                    Text(
                        text="200 pt",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                }

            }

            Card(
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f)
                    .padding(horizontal = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(id = R.drawable.jama3a),
                        modifier = Modifier.size(30.dp),
                        contentDescription = "Dark Theme Icon"
                    )
                    Text(
                        text="Group",
                        fontSize = 12.sp
                    )
                    Text(
                        text="20 %",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                }

            }
        }

    }

}