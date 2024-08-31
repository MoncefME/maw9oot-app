package com.example.maw9oot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
//import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.maw9oot.data.local.DataStoreManager
import com.example.maw9oot.presentation.navigation.NavigationGraph
import com.example.maw9oot.presentation.ui.layout.BottomNavBar
import com.example.maw9oot.presentation.ui.layout.TopBar
import com.example.maw9oot.presentation.ui.layout.bottomNavItems
import com.example.maw9oot.presentation.ui.utils.BiometricsPromptManager
import com.example.maw9oot.ui.theme.Maw9ootTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private val promptManager by lazy {
        BiometricsPromptManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val initialSecurity = dataStoreManager.getInitialSecurity()
        val initialDarkTheme = dataStoreManager.getInitialDarkTheme()

        setContent {
            var isDarkTheme by remember { mutableStateOf(initialDarkTheme) }
            var isSecurityEnabled by remember { mutableStateOf(initialSecurity) }
            var authenticationSuccess by remember { mutableStateOf(false) }
            var showPrompt by remember { mutableStateOf(isSecurityEnabled) }

            LaunchedEffect(Unit) {
                dataStoreManager.isDarkTheme.collectLatest { theme ->
                    isDarkTheme = theme
                }
                dataStoreManager.isSecurityEnabled.collectLatest { security ->
                    isSecurityEnabled = security
                    showPrompt = security && !authenticationSuccess
                }
            }

            if (isSecurityEnabled && showPrompt) {
                LaunchedEffect(showPrompt) {
                    promptManager.showBiometricPrompt(
                        title = "Authentication Required",
                        description = "Please authenticate to proceed"
                    )
                    promptManager.promptResults.collect { result ->
                        when (result) {
                            is BiometricsPromptManager.BiometricResult.AuthenticationSuccess -> {
                                authenticationSuccess = true
                                showPrompt = false
                            }
                            else -> {
                                authenticationSuccess = false
                                showPrompt = false
                            }
                        }
                    }
                }
            }

            Maw9ootTheme(darkTheme = isDarkTheme) {
                if (authenticationSuccess || !isSecurityEnabled) {
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
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(id = R.drawable.baseline_fingerprint_24),
                                contentDescription ="Dark Theme Icon"
                            )
                            Text(
                                text = "You need to Authenticate to access Maw9oot",
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.Center
                            )

                            Button(onClick = {
                                showPrompt = true
                            }) {
                                Text(text = "Authenticate")
                            }
                        }
                    }
                }
            }
        }
    }
}
