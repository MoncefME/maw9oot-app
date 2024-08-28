package com.example.maw9oot.presentation.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.maw9oot.presentation.viewmodel.StatViewModel

@Composable
fun SettingScreen(
    navController: NavController,
    settingViewModel: StatViewModel
) {
    Text(text = "Settings Screen")
}