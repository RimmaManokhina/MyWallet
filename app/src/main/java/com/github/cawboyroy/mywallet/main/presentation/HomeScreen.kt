package com.github.cawboyroy.mywallet.main.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.github.cawboyroy.mywallet.R
import androidx.activity.compose.BackHandler

@Composable
fun HomeScreen(navController: NavController) {
    BackHandler { }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add") }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = stringResource(R.string.add)
                )
            }
        }
    ) { contentPadding ->
        ListContent(contentPadding) { recordId ->
            navController.navigate("edit/$recordId")
        }
    }
}