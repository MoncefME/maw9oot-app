package com.example.maw9oot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.maw9oot.data.local.DataStoreManager
import com.example.maw9oot.presentation.navigation.NavigationGraph
import com.example.maw9oot.presentation.ui.layout.BottomNavBar
import com.example.maw9oot.presentation.ui.layout.TopBar
import com.example.maw9oot.presentation.ui.screens.AuthenticationScreen
import com.example.maw9oot.data.utils.BiometricsPromptManager
import com.example.maw9oot.ui.theme.Maw9ootTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreManager: DataStoreManager

    private val promptManager by lazy {
        BiometricsPromptManager(this)
    }

    private var wasInBackground by mutableStateOf(false)
    private var isSecurityEnabled by mutableStateOf(false)
    private var authenticationSuccess by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        lifecycleScope.launch {
            val savedLanguage = dataStoreManager.language.first()
            setLocale(savedLanguage)
        }

        setContent {
            val darkTheme by dataStoreManager.isDarkTheme.collectAsState(initial = false)
            val securityEnabled by dataStoreManager.isSecurityEnabled.collectAsState(initial = false)

            isSecurityEnabled = securityEnabled

            MainContent(darkTheme, authenticationSuccess)
        }
    }

    override fun onResume() {
        super.onResume()
        if (wasInBackground && isSecurityEnabled) {
            promptBiometricAuthentication()
        }
        wasInBackground = false
    }

    override fun onPause() {
        super.onPause()
        wasInBackground = true
    }


    @Composable
    private fun MainContent(isDarkTheme: Boolean, authenticationSuccess: Boolean) {
        Maw9ootTheme(darkTheme = isDarkTheme) {
            if (authenticationSuccess || !isSecurityEnabled) {
                val navController = rememberNavController()
                Scaffold(
                    topBar = { TopBar(navController = navController) },
                    bottomBar = { BottomNavBar(navController = navController) }
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
                AuthenticationScreen(onAuthenticateClick = { promptBiometricAuthentication() })
            }
        }
    }


    private fun setLocale(language: String) {
        val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }


    private fun promptBiometricAuthentication() {
        promptManager.showBiometricPrompt(
            title = "Authentication Required",
            description = "Please authenticate to proceed"
        )

        lifecycleScope.launch {
            promptManager.promptResults.collect { result ->
                authenticationSuccess = when (result) {
                    is BiometricsPromptManager.BiometricResult.AuthenticationSuccess -> {
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
        }
    }
}