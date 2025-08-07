package com.github.cawboyroy.mywallet.bars.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.add.presentation.AnimatedIncomeExpenseToggle

@Composable
fun TopUi(
    onLeftButtonClick: () -> Unit,
    onRightButtonClick: () -> Unit,
    isExpenses: Boolean,
    changeIsExpenses: (Boolean) -> Unit,
    @StringRes rightContentDescriptionResId: Int = R.string.next_month,
    @StringRes leftContentDescriptionResId: Int = R.string.previous_month
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Button(
            onClick = onLeftButtonClick,
            modifier = Modifier
                .padding(4.dp)
                .testTag("LeftButton")
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(leftContentDescriptionResId)
            )
        }

        AnimatedIncomeExpenseToggle(
            Modifier.weight(1f),
            if (isExpenses) 0 else 1
        ) {
            changeIsExpenses(it == 0)
        }

        Button(onClick = onRightButtonClick, modifier = Modifier.padding(4.dp)) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = stringResource(rightContentDescriptionResId)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTopUiIncomes() {
    TopUi({}, {}, false, {})
}