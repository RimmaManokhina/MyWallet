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
import androidx.compose.ui.platform.testTag

@Composable
fun DayUi(
    index: Int,
    modifier: Modifier,
    @StringRes description: Int,
    icon: ImageVector,
    date: String,
    sum: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.Companion.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(text = date, modifier = Modifier.Companion
            .weight(1f)
            .testTag("DayUiDate at $index"))
        Text(
            text = sum, modifier = Modifier
                .testTag("DayUiSum at $index")
                .padding(horizontal = 4.dp)
                .testTag("DaySum")
        )

        Button(onClick = onClick) {
            Icon(
                icon,
                contentDescription = stringResource(description)
            )
        }
    }
}