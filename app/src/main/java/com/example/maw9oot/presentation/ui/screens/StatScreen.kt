package com.example.maw9oot.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.maw9oot.presentation.ui.components.stats.challenges.ChallengesList
import com.example.maw9oot.presentation.ui.components.stats.prayerLogs.PrayerGrid
import com.example.maw9oot.presentation.ui.components.stats.scoarboard.ScoreLeaderboard


@Composable
fun StatScreen() {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { ScoreLeaderboard() }
        item { ChallengesList() }
        item { PrayerGrid() }
    }
}

