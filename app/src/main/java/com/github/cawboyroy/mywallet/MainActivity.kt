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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.github.cawboyroy.mywallet.main.presentation.ListContent
import com.github.cawboyroy.mywallet.ui.theme.MyWalletTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.mutableLongStateOf
import com.github.cawboyroy.mywallet.main.presentation.EditFinancialRecord

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWalletTheme {
                var showAddBottomSheet by rememberSaveable { mutableStateOf(false) }
                var editRecordId by rememberSaveable { mutableLongStateOf(-1L) }
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { showAddBottomSheet = true }
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = stringResource(R.string.add)
                            )
                        }
                    }
                ) { contentPadding ->
                    ListContent(contentPadding) { id -> editRecordId = id }

                    if (showAddBottomSheet) //todo replace by navController
                        AddFinancialRecord { showAddBottomSheet = false }
                    else if (editRecordId != -1L) {
                        EditFinancialRecord(editRecordId) { editRecordId = -1 }
                    }
                }
            }
        }
    }
}