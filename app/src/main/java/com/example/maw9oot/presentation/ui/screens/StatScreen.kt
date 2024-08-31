package com.example.maw9oot.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.maw9oot.presentation.ui.components.stats.Challenge
import com.example.maw9oot.presentation.ui.components.stats.ChallengeCard
import com.example.maw9oot.presentation.ui.components.stats.PrayerGrid
import com.example.maw9oot.presentation.ui.components.stats.PrayerStatsCard
import com.example.maw9oot.presentation.viewmodel.StatViewModel

@Composable
fun StatScreen(
    statViewModel: StatViewModel = hiltViewModel()
) {

    val days by statViewModel.days.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        PrayerGrid(days = days)
        val fajrChallenge = Challenge(
            prayer = "Fajr",
            month = "June",
            description = "Maintain a streak of Fajr prayers every day.",
            currentStreak = 15,
            maxStreak = 30
        )

        ChallengeCard(challenge = fajrChallenge)
        PrayerStatsCard()
    }
}

