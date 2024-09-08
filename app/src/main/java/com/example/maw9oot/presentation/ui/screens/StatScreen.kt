package com.example.maw9oot.presentation.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.maw9oot.presentation.ui.components.stats.ChallengesList
import com.example.maw9oot.presentation.ui.components.stats.PrayerGrid
import com.example.maw9oot.presentation.ui.components.stats.PrayerStatsCard
import com.example.maw9oot.presentation.viewmodel.StatViewModel

@Composable
fun StatScreen(
    statViewModel: StatViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 16.dp),
    ) {
        ChallengesList()
        PrayerGrid(statViewModel = statViewModel)
//        PrayerStatsCard()
    }
}

