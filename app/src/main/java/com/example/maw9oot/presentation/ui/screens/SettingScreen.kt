package com.example.maw9oot.presentation.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.maw9oot.presentation.ui.components.SelectionType
import com.example.maw9oot.presentation.ui.components.ToggleButton
import com.example.maw9oot.presentation.ui.components.ToggleButtonOption
import com.example.maw9oot.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel
) {
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState(initial = false)
    val language by settingsViewModel.language.collectAsState(initial = "en")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val themeOptions  = listOf(
            ToggleButtonOption("Dark"),
            ToggleButtonOption("Light")
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Dark Theme")
            ToggleButton(
                options = themeOptions,
                type = SelectionType.SINGLE,
                onClick = { selectedOption ->
                    settingsViewModel.toggleTheme(selectedOption.label == "Dark")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val languageOptions = listOf(
            ToggleButtonOption("English"),
            ToggleButtonOption("Arabic")
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Language ")
            ToggleButton(
                options = languageOptions,
                type = SelectionType.SINGLE,
                onClick = { selectedOption ->
                    settingsViewModel.setLanguage(if (selectedOption.label == "English") "en" else "ar")
                }
            )
        }

    }
}