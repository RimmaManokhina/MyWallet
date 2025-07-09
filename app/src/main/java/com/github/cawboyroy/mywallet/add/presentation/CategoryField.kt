package com.github.cawboyroy.mywallet.add.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.cawboyroy.mywallet.R

@Composable
fun CategoryField(value: String, isExpenses: Boolean, onValueChanged: (String) -> Unit) {
    Title(R.string.category)
    var hasFocus by rememberSaveable { mutableStateOf(false) }
    BasicTextField(
        cursorBrush = SolidColor(LocalContentColor.current),
        textStyle = TextStyle(fontSize = 18.sp, color = LocalContentColor.current),
        value = TextFieldValue(value, selection = TextRange(value.length)),
        onValueChange = {
            onValueChanged.invoke(it.text)
        },
        modifier = Modifier.Companion
            .onFocusChanged {
                hasFocus = it.hasFocus
            }
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Companion.Gray),
        singleLine = true,
        decorationBox = {
            Box(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(4.dp),
                contentAlignment = Alignment.Companion.CenterStart
            ) {
                it()
            }
        }
    )
    Spacer(modifier = Modifier.height(4.dp))
    val context = LocalContext.current
    if (hasFocus) HorizontallyScrollableTwoRowIconGridWithTitles(
        icons = if (isExpenses) expensesCategoryList else incomeCategoryList,
        onIconClick = {
            onValueChanged.invoke(context.getString(it.titleResId))
        }
    )
}

data class CategoryIcon(
    val id: Int,
    @DrawableRes val iconResId: Int,
    @StringRes val titleResId: Int,
)

val expensesCategoryList = listOf(
    CategoryIcon(1, R.drawable.ic_category_groceries, R.string.category_groceries),
    CategoryIcon(2, R.drawable.ic_category_dining_out, R.string.category_dining_out),
    CategoryIcon(3, R.drawable.ic_category_transport, R.string.category_transport),
    CategoryIcon(4, R.drawable.ic_category_utilities, R.string.category_utilities),
    CategoryIcon(5, R.drawable.ic_category_mortgage, R.string.category_mortgage),
    CategoryIcon(6, R.drawable.ic_category_shopping, R.string.category_shopping),
    CategoryIcon(7, R.drawable.ic_category_entertainment, R.string.category_entertainment),
    CategoryIcon(8, R.drawable.ic_category_health_n_fitness, R.string.category_health_n_fitness),
    CategoryIcon(9, R.drawable.ic_category_education, R.string.category_education),
    CategoryIcon(10, R.drawable.ic_category_personal_care, R.string.category_personal_care),
    CategoryIcon(11, R.drawable.ic_category_travel, R.string.category_travel),
    CategoryIcon(11, R.drawable.ic_category_insurance, R.string.category_insurance),
    CategoryIcon(12, R.drawable.ic_category_debt_payments, R.string.category_debts),
    CategoryIcon(13, R.drawable.ic_category_home_maintenance, R.string.category_home_maintenance),
    CategoryIcon(14, R.drawable.ic_category_kids, R.string.category_kids),
    CategoryIcon(15, R.drawable.ic_category_pets, R.string.category_pets),
    CategoryIcon(16, R.drawable.ic_category_gifts, R.string.category_gifts),
    CategoryIcon(17, R.drawable.ic_category_subscriptions, R.string.category_subscriptions),
    CategoryIcon(18, R.drawable.ic_category_taxes, R.string.category_taxes),
    CategoryIcon(19, R.drawable.ic_category_savings, R.string.category_savings),
    CategoryIcon(20, R.drawable.ic_category_other, R.string.category_other),
)
val incomeCategoryList = listOf(
    CategoryIcon(21, R.drawable.ic_money_bag, R.string.category_salary),
    CategoryIcon(22, R.drawable.ic_payment_arrow_down, R.string.category_freelance),
    CategoryIcon(23, R.drawable.ic_money_bag, R.string.category_business_profit),
    CategoryIcon(24, R.drawable.ic_category_mortgage, R.string.category_rental_income),
    CategoryIcon(25, R.drawable.ic_checkbook, R.string.category_interest_income),
    CategoryIcon(26, R.drawable.ic_universal_currency, R.string.category_dividend_income),
    CategoryIcon(27, R.drawable.ic_money_bag, R.string.category_bonuses),
    CategoryIcon(28, R.drawable.ic_universal_currency, R.string.category_commissions),
    CategoryIcon(29, R.drawable.ic_payment_arrow_down, R.string.category_tips),
    CategoryIcon(30, R.drawable.ic_category_gifts, R.string.category_gifts),
    CategoryIcon(31, R.drawable.ic_category_other, R.string.category_other_income)
)

private val MAX_ITEM_SIZE = 100.dp

@Composable
fun HorizontallyScrollableTwoRowIconGridWithTitles(
    icons: List<CategoryIcon>,
    onIconClick: (CategoryIcon) -> Unit,
    modifier: Modifier = Modifier,
    iconSize: Dp = 36.dp,
    itemPadding: Dp = 8.dp
) {
    val iconPairs = icons.chunked(2)

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        contentPadding = PaddingValues(horizontal = itemPadding / 2),
        horizontalArrangement = Arrangement.spacedBy(itemPadding / 2)
    ) {
        items(
            items = iconPairs,
            key = { pair -> pair.first().id }
        ) { iconPair ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SingleIconWithTitleView(
                    iconData = iconPair[0],
                    onIconClick = onIconClick,
                    iconSize = iconSize,
                    itemPadding = itemPadding
                )

                Spacer(modifier = Modifier.height(itemPadding))

                if (iconPair.size > 1)
                    SingleIconWithTitleView(
                        iconData = iconPair[1],
                        onIconClick = onIconClick,
                        iconSize = iconSize,
                        itemPadding = itemPadding
                    )
                else
                    Box(
                        modifier = Modifier
                            .width(MAX_ITEM_SIZE)
                            .height(MAX_ITEM_SIZE)
                    )
            }
        }
    }
}

@Composable
private fun SingleIconWithTitleView(
    iconData: CategoryIcon,
    onIconClick: (CategoryIcon) -> Unit,
    iconSize: Dp,
    itemPadding: Dp
) {
    Column(
        modifier = Modifier
            .width(MAX_ITEM_SIZE)
            .height(MAX_ITEM_SIZE)
            .clickable { onIconClick(iconData) }
            .padding(itemPadding / 2)
            .border(
                width = 1.dp,
                color = LocalContentColor.current,
                shape = RoundedCornerShape(4.dp)
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconData.iconResId),
            contentDescription = stringResource(iconData.titleResId),
            modifier = Modifier.size(iconSize),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(iconData.titleResId),
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}