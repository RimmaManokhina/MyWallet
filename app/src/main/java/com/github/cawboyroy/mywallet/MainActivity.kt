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
import com.github.cawboyroy.mywallet.add.presentation.AddFinancialRecordScreen
import com.github.cawboyroy.mywallet.currency.presentation.ChooseCurrencyScreen
import com.github.cawboyroy.mywallet.edit.presentation.EditFinancialRecordScreen
import com.github.cawboyroy.mywallet.main.presentation.MainScreen
import com.github.cawboyroy.mywallet.settings.presentation.ImportExportScreen
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

                NavHost(navController = navController, startDestination = "main") {
                    composable("main") { MainScreen(navController) }
                    composable("choose currency") { ChooseCurrencyScreen(navController) }
                    composable("import export") { ImportExportScreen() }
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