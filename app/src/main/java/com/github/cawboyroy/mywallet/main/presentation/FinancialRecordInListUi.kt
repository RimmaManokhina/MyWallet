package com.github.cawboyroy.mywallet.main.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.semantics

@Composable
fun FinancialRecordInListUi(
    index: Int,
    modifier: Modifier,
    @DrawableRes categoryId: Int,
    id: Long,
    isExpenses: Boolean,
    money: String,
    title: String,
    category: String,
    onClick: (Long) -> Unit
) {
    Card(
        modifier = modifier
            .testTag("FinRecord at $index")
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable(enabled = true, onClick = { onClick.invoke(id) }),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(categoryId),
                contentDescription = category,
                modifier = Modifier
                    .size(36.dp)
                    .testTag("RecordIcon at $index")
                    .semantics {
                        this.drawableResId = categoryId
                    },
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.testTag("RecordTitle at $index"),
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier.testTag("RecordCategory at $index"),
                    text = category,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    maxLines = 1
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )

            Text(
                text = money,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = Color(if (isExpenses) 0xFFFF0000 else 0xFF008000),
                textAlign = TextAlign.End,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .testTag("RecordMoney at $index")
            )
        }
    }
}

val DrawableResId = SemanticsPropertyKey<Int>("DrawableResId")

var SemanticsPropertyReceiver.drawableResId by DrawableResId // Extension property for easy use