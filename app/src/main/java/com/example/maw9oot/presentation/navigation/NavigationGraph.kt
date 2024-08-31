package com.example.maw9oot.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.maw9oot.presentation.ui.screens.StatScreen
import com.example.maw9oot.presentation.ui.screens.HomeScreen
import com.example.maw9oot.presentation.ui.screens.SettingScreen
import com.example.maw9oot.presentation.viewmodel.HomeViewModel
import com.example.maw9oot.presentation.viewmodel.SettingsViewModel
import com.example.maw9oot.presentation.viewmodel.StatViewModel

@Composable
fun NavigationGraph(navController: NavHostController){

    val homeViewModel = hiltViewModel<HomeViewModel>()
    val statViewModel = hiltViewModel<StatViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()

    NavHost(navController = navController, startDestination = Screens.HomeScreen.route){
        composable(route = Screens.HomeScreen.route){
            HomeScreen(navController =navController,homeViewModel = homeViewModel)
        }
        composable(route = Screens.StatScreen.route){
            StatScreen(navController = navController,statViewModel = statViewModel)
        }
        composable(route = Screens.SettingScreen.route){
            SettingScreen(navController = navController,settingsViewModel = settingsViewModel)
        }
    }

}