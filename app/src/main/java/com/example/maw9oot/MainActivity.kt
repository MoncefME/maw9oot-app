package com.example.maw9oot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.maw9oot.data.local.DataStoreManager
import com.example.maw9oot.presentation.navigation.NavigationGraph
import com.example.maw9oot.presentation.ui.layout.BottomNavBar
import com.example.maw9oot.presentation.ui.layout.TopBar
import com.example.maw9oot.presentation.ui.layout.bottomNavItems
import com.example.maw9oot.ui.theme.Maw9ootTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                dataStoreManager.isDarkTheme.collectLatest { theme ->
                    isDarkTheme = theme
                }
            }

            Maw9ootTheme(darkTheme = isDarkTheme) {
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
