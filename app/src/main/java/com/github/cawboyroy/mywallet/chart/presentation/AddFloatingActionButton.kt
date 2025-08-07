package com.github.cawboyroy.mywallet.chart.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.github.cawboyroy.mywallet.R

@Composable
fun AddFloatingActionButton(navController: NavController) {
    FloatingActionButton(
        modifier = Modifier.Companion.testTag("AddFAB"),
        onClick = { navController.navigate("add") }
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription = stringResource(R.string.add)
        )
    }
}