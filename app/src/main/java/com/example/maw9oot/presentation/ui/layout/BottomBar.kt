package com.example.maw9oot.presentation.ui.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.maw9oot.presentation.navigation.Screens
import com.example.maw9oot.R

@Composable
fun BottomNavBar(navController: NavHostController) {
    val bottomNavItems = listOf(
        BottomNavItem(
            title = stringResource(id = R.string.home),
            route = Screens.HomeScreen.route,
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavItem(
            title = stringResource(id = R.string.stats),
            route = Screens.StatScreen.route,
            selectedIcon = Icons.Filled.Menu,
            unselectedIcon = Icons.Filled.Menu
        ),
        BottomNavItem(
            title = stringResource(id = R.string.settings),
            route = Screens.SettingScreen.route,
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings
        ),
    )

    var selected by remember { mutableIntStateOf(0) }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        bottomNavItems.forEachIndexed { index, bottomNavItem ->
            NavigationBarItem(
                selected = selected == index,
                onClick = {
                    selected = index
                    navController.navigate(bottomNavItem.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (selected == index) bottomNavItem.selectedIcon else bottomNavItem.unselectedIcon,
                        contentDescription = null,
                    )
                },
                label = {
                    Text(
                        text = bottomNavItem.title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    unselectedIconColor = MaterialTheme.colorScheme.inversePrimary,
                    selectedTextColor = MaterialTheme.colorScheme.inversePrimary,
                    unselectedTextColor = MaterialTheme.colorScheme.inversePrimary
                )
            )
        }
    }
}

data class BottomNavItem(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

//val bottomNavItems = listOf(
//    BottomNavItem(
//        title = "Home",
//        route = Screens.HomeScreen.route,
//        selectedIcon = Icons.Filled.Home,
//        unselectedIcon = Icons.Outlined.Home
//    ),
//    BottomNavItem(
//        title = "Stats",
//        route = Screens.StatScreen.route,
//        selectedIcon = Icons.Filled.Menu,
//        unselectedIcon = Icons.Filled.Menu
//    ),
//    BottomNavItem(
//        title = "Settings",
//        route = Screens.SettingScreen.route,
//        selectedIcon = Icons.Filled.Settings,
//        unselectedIcon = Icons.Outlined.Settings
//    ),
//)
