package com.example.maw9oot.presentation.navigation

sealed class Screens(val route: String)  {
    data object HomeScreen :Screens("HomeScreen")
    data object StatScreen :Screens("StatScreen")
    data object SettingScreen :Screens("SettingScreen")
}