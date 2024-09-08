package com.example.maw9oot.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.maw9oot.presentation.ui.screens.StatScreen
import com.example.maw9oot.presentation.ui.screens.HomeScreen
import com.example.maw9oot.presentation.ui.screens.SettingScreen

@Composable
fun NavigationGraph(navController: NavHostController){

    NavHost(navController = navController, startDestination = Screens.HomeScreen.route){
        composable(route = Screens.HomeScreen.route){
            HomeScreen()
        }
        composable(route = Screens.StatScreen.route){
            StatScreen()
        }
        composable(route = Screens.SettingScreen.route){
            SettingScreen()
        }
    }
}