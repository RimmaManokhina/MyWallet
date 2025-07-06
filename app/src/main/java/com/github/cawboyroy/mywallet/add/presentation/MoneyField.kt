package com.github.cawboyroy.mywallet.add.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.cawboyroy.mywallet.R
import java.math.BigInteger

@Composable
fun MoneyField(edit: Boolean, value: String, onValueChanged: (String) -> Unit) {
    Title(R.string.money)
    val bigDecimal: Pair<BigInteger, String> = HandleMoney.split(value)
    val mainPart: BigInteger = bigDecimal.first
    val centsPart: String = bigDecimal.second

    val firstText by remember { mutableStateOf(value) }

    val text = HandleMoney.formatMainPart(mainPart)

    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.width(16.dp))
        BasicTextField(
            cursorBrush = SolidColor(LocalContentColor.current),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = 18.sp,
                textAlign = TextAlign.End,
                color = LocalContentColor.current
            ),
            value = TextFieldValue(text, selection = TextRange(text.length)),
            onValueChange = {
                val new: String = it.text
                if (new != text)
                    onValueChanged(HandleMoney.handleMainPart(new) + "." + centsPart)
            },
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .border(1.dp, Color.Gray),
            singleLine = true,
            decorationBox = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    it()
                }
            }
        )
        Spacer(modifier = Modifier.width(4.dp))
        BasicTextField(
            cursorBrush = SolidColor(LocalContentColor.current),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                color = LocalContentColor.current
            ),
            value = centsPart,
            onValueChange = {
                onValueChanged(mainPart.toString() + "." + HandleMoney.handleCents(it))
            },
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .border(1.dp, Color.Gray),
            singleLine = true,
            decorationBox = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    it()
                }
            }
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
    if (edit && firstText != value) SelectionContainer {
        Text(
            text = HandleMoney.formatWhole(firstText),
            textAlign = TextAlign.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )
    }

}