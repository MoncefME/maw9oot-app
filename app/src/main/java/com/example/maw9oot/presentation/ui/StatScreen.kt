package com.example.maw9oot.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.maw9oot.presentation.viewmodel.StatViewModel

@Composable
fun StatScreen(
    navController: NavController,
    statViewModel: StatViewModel
) {

    val prayerLogs by statViewModel.prayerLogs.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(prayerLogs) { log ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Prayer: ${log.prayerType}")
                Text(text = "Status: ${log.status}")
                Text(text = "Date: ${log.date}")
            }
        }
    }
}
