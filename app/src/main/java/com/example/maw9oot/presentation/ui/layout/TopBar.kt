package com.example.maw9oot.presentation.ui.layout


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.maw9oot.presentation.navigation.Screens
import java.text.SimpleDateFormat
import com.example.maw9oot.R
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    val title = when (currentRoute) {
        Screens.HomeScreen.route -> stringResource(id = R.string.home)
        Screens.StatScreen.route -> stringResource(id = R.string.stats)
        Screens.SettingScreen.route -> stringResource(id = R.string.settings)
        else -> "Maw9oot"
    }

    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            MaterialTheme.colorScheme.primary
        )
    )
}

