package com.github.cawboyroy.mywallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.github.cawboyroy.mywallet.add.presentation.AddExpenses
import com.github.cawboyroy.mywallet.add.presentation.ListContent
import com.github.cawboyroy.mywallet.ui.theme.MyWalletTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWalletTheme {
                var showBottomSheet by remember { mutableStateOf(false) }
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { showBottomSheet = true }
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = stringResource(R.string.add)
                            )
                        }
                    }
                ) { contentPadding ->
                    ListContent(contentPadding)

                    if (showBottomSheet) //todo replace by navController
                        AddExpenses { showBottomSheet = false }
                }
            }
        }
    }
}