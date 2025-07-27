package com.github.cawboyroy.mywallet.add.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.cawboyroy.mywallet.R

@Composable
fun Title(@StringRes resourceId: Int) {
    Text(
        stringResource(resourceId),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 16.dp, top = 8.dp)
    )
}

@Composable
fun TitleField(value: String, onValueChanged: (String) -> Unit) {
    Title(R.string.title)
    BasicTextField(
        cursorBrush = SolidColor(LocalContentColor.current),
        textStyle = TextStyle(fontSize = 18.sp, color = LocalContentColor.current),
        value = value,
        onValueChange = onValueChanged,
        modifier = Modifier
            .testTag("DescriptionInputField")
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Gray),
        singleLine = true,
        decorationBox = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                it()
            }
        }
    )
}

@Composable
fun CategoryField(value: String, onValueChanged: (String) -> Unit) {
    Title(R.string.category)
    BasicTextField(
        cursorBrush = SolidColor(LocalContentColor.current),
        textStyle = TextStyle(fontSize = 18.sp, color = LocalContentColor.current),
        value = value,
        onValueChange = onValueChanged,
        modifier = Modifier
            .height(48.dp)
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Gray),
        singleLine = true,
        decorationBox = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                it()
            }
        }
    )
}

@Composable
fun DescriptionField(value: String, onValueChanged: (String) -> Unit) {
    Title(R.string.description)
    BasicTextField(
        cursorBrush = SolidColor(LocalContentColor.current),
        textStyle = TextStyle(fontSize = 18.sp, color = LocalContentColor.current),
        value = value,
        onValueChange = onValueChanged,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .border(1.dp, Color.Gray),
        singleLine = false,
        minLines = 2,
        maxLines = 4,
        decorationBox = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                contentAlignment = Alignment.TopStart
            ) {
                it()
            }
        }
    )
}