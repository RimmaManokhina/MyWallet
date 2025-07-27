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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.testTag("HomeAddButton"),
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