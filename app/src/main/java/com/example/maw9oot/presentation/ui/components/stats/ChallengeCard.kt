package com.example.maw9oot.presentation.ui.components.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Challenge(
    val month: String,
    val prayer: String,
    val description: String,
    val currentStreak: Int,
    val maxStreak: Int
)

@Composable
fun ChallengeCard(challenge: Challenge) {
    val title = "${challenge.month}  ${challenge.prayer} Challenge"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                color = Color.Black,
            )

            Text(
                text = challenge.description,
                fontSize = 14.sp,
                color = Color.Gray,
            )

            Text(
                text = "${challenge.currentStreak}/${challenge.maxStreak} days",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val progress = (challenge.currentStreak.toFloat() / challenge.maxStreak.toFloat())
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.Gray.copy(alpha = 0.2f))
            ) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(15.dp)),
                    color = Color(0xFF13b601),
                    trackColor = Color.Transparent,
                )
            }
        }
    }
}
