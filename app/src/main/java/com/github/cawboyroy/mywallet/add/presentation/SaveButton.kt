package com.github.cawboyroy.mywallet.add.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cawboyroy.mywallet.R

@Composable
fun SaveButton(enabled: Boolean, onClick: () -> Unit) {
    Button(
        enabled = enabled,
        modifier = Modifier.Companion
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        onClick = onClick
    ) {
        Text(stringResource(R.string.save))
    }
}