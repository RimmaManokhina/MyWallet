package com.github.cawboyroy.mywallet.add.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalContentColor
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.cawboyroy.mywallet.R

@Composable
fun TitleField(
    value: String,
    isExpenses: Boolean,
    changeCategory: (String) -> Unit,
    onValueChanged: (String) -> Unit
) {
    Title(R.string.title)
    var hasFocus by rememberSaveable { mutableStateOf(false) }
    BasicTextField(
        cursorBrush = SolidColor(LocalContentColor.current),
        textStyle = TextStyle(fontSize = 18.sp, color = LocalContentColor.current),
        value = TextFieldValue(value, selection = TextRange(value.length)),
        onValueChange = {
            onValueChanged.invoke(it.text)
        },
        modifier = Modifier
            .testTag("RecordTitleInputField")
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
    val titleTrimmed: String = value.trim()
    if (hasFocus && titleTrimmed.isNotEmpty())
        FinancialRecordSuggestions(titleTrimmed, isExpenses) { (title, category) ->
            onValueChanged.invoke(title)
            changeCategory.invoke(category)
        }
}