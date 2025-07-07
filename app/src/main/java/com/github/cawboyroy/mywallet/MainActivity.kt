package com.github.cawboyroy.mywallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.github.cawboyroy.mywallet.add.presentation.AddFinancialRecordScreen
import com.github.cawboyroy.mywallet.currency.presentation.ChooseCurrencyScreen
import com.github.cawboyroy.mywallet.currency.presentation.ChooseCurrencyViewModel
import com.github.cawboyroy.mywallet.edit.presentation.EditFinancialRecordScreen
import com.github.cawboyroy.mywallet.main.presentation.HomeScreen
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
                val viewModel = hiltViewModel<ChooseCurrencyViewModel>()
                val start = if (viewModel.chosenCurrency().isEmpty()) "chose currency" else "home"
                NavHost(navController = navController, startDestination = start) {
                    composable("home") { HomeScreen(navController) }
                    composable("chose currency") { ChooseCurrencyScreen(navController) }
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