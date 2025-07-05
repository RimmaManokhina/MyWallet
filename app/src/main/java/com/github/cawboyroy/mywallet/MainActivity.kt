package com.github.cawboyroy.mywallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.cawboyroy.mywallet.edit.data.presentation.presentation.EditFinancialRecordScreen
import com.github.cawboyroy.mywallet.add.presentation.AddFinancialRecordScreen
import com.github.cawboyroy.mywallet.ui.theme.MyWalletTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWalletTheme {
                val navController: NavHostController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(navController) }
                    composable("add") { AddFinancialRecordScreen(navController) }
                    composable(
                        route = "edit/{recordId}",
                        arguments = listOf(navArgument("recordId") { type = NavType.LongType })
                    ) { backStackEntry ->
                        val recordId = backStackEntry.arguments?.getLong("recordId")
                            ?: throw IllegalStateException("recordId is null")
                        EditFinancialRecordScreen(recordId, navController)
                    }
                }
            }
        }
    }
}