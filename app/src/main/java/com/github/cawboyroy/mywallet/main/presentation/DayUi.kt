package com.github.cawboyroy.mywallet.main.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun DayUi(
    @StringRes description: Int,
    icon: ImageVector,
    date: String,
    sum: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(text = date, modifier = Modifier.Companion.weight(1f))
        Text(text = sum, modifier = Modifier.Companion.padding(horizontal = 4.dp))
        Button(onClick = onClick) {
            Icon(
                icon,
                contentDescription = stringResource(description)
            )
        }
    }
}