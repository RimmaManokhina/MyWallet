package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.bars.BarsScreen
import com.github.cawboyroy.mywallet.chart.presentation.ChartScreen
import com.github.cawboyroy.mywallet.settings.presentation.SettingsScreen

@Composable
fun MainScreen(outerNavController: NavController) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Home.route) { HomeScreen(outerNavController) }
            composable(BottomNavItem.Chart.route) { ChartScreen(outerNavController) }
            composable(BottomNavItem.Bars.route) { BarsScreen() }
            composable(BottomNavItem.Settings.route) { SettingsScreen(outerNavController) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Chart,
        BottomNavItem.Bars,
        BottomNavItem.Settings
    )
    var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    LaunchedEffect(currentRoute) {
        items.indexOfFirst { it.route == currentRoute }.takeIf { it != -1 }?.let {
            selectedItemIndex = it
        }
    }

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItemIndex == index,
                onClick = {
                    if (selectedItemIndex != index) {
                        selectedItemIndex = index
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                label = { Text(item.label) },
                icon = { item.IconUi() }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    @Composable
    open fun IconUi() = Icon(icon, contentDescription = label)

    object Bars : BottomNavItem("bars", Icons.Filled.Home, "Bars") {

        @Composable
        override fun IconUi() =
            Icon(painter = painterResource(R.drawable.ic_bars), contentDescription = label)
    }

    object Home : BottomNavItem("home", Icons.Filled.Home, "Home")

    object Chart : BottomNavItem("chart", Icons.Filled.Home, "Chart") {

        @Composable
        override fun IconUi() =
            Icon(painter = painterResource(R.drawable.ic_chart), contentDescription = label)
    }

    object Settings : BottomNavItem(
        "settings",
        Icons.Filled.Settings, "Settings"
    )
}