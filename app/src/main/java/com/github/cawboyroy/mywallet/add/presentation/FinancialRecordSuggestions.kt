package com.github.cawboyroy.mywallet.add.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.cawboyroy.mywallet.R
import java.io.Serializable

@Composable
fun FinancialRecordSuggestions(
    title: String,
    isExpenses: Boolean,
    onClick: (Pair<String, String>) -> Unit
) {
    val viewModel = hiltViewModel<FinancialRecordSuggestionsViewModel>()

    LaunchedEffect(title, isExpenses) {
        viewModel.find(title, isExpenses)
    }

    val list by viewModel.state.collectAsStateWithLifecycle()
    if (list.isNotEmpty()) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = list,
                key = { item -> item.id() }
            ) { suggestion ->
                suggestion.Show(onClick)
            }
        }
    }
}

interface FinancialRecordSuggestionUi : Serializable {

    @Composable
    fun Show(onClick: (Pair<String, String>) -> Unit)

    fun id(): String

    data class Base(
        private val title: String,
        private val category: String,
        @DrawableRes
        private val categoryIconResId: Int
    ) : FinancialRecordSuggestionUi {

        @Composable
        override fun Show(onClick: (Pair<String, String>) -> Unit) {
            Row(
                modifier = Modifier
                    .clickable {
                        onClick.invoke(Pair(title, category))
                    }
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(categoryIconResId),
                    contentDescription = category,
                    modifier = Modifier.size(36.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.padding(horizontal = 2.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        override fun id() = title + category
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFinancialRecordSuggestionUi() {
    FinancialRecordSuggestionUi.Base("bread", "Groceries", R.drawable.ic_category_groceries).Show {
    }
}