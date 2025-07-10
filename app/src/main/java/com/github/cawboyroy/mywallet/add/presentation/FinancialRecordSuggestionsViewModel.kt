package com.github.cawboyroy.mywallet.add.presentation

import android.content.Context
import com.github.cawboyroy.mywallet.R

private fun Context.categoryResId(category: String, isExpense: Boolean): Int {
    val resourceName = buildString {
        append(if (isExpense) "ic_expense_" else "ic_income_")
        append(category.lowercase().replace(" ", "_"))
    }

    val resId = resources.getIdentifier(resourceName, "drawable", packageName)

    return if (resId != 0) {
        resId
    } else {
        // Резервная иконка по умолчанию
        if (isExpense) R.drawable.ic_category_other else R.drawable.ic_money_bag
    }
}