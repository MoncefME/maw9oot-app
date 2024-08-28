package com.example.maw9oot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import com.example.maw9oot.presentation.navigation.NavigationGraph
import com.example.maw9oot.presentation.ui.components.BottomNavBar
import com.example.maw9oot.presentation.ui.components.TopBar
import com.example.maw9oot.presentation.ui.components.bottomNavItems
import com.example.maw9oot.ui.theme.Maw9ootTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Maw9ootTheme {
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        TopBar(navController = navController)
                    },
                    bottomBar = {
                        BottomNavBar(navController = navController, items = bottomNavItems)
                    }
                ) { paddingValues ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        NavigationGraph(navController = navController)
                    }
                }
            }
        }
    }
}
