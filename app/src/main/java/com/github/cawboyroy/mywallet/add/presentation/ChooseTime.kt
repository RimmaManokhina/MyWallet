package com.github.cawboyroy.mywallet.add.presentation

import android.icu.util.Calendar
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.cawboyroy.mywallet.R
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseTime(value: Long, onValueChanged: (Long) -> Unit) {

    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDateMillis by remember { mutableLongStateOf(0) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value,
        initialDisplayMode = DisplayMode.Picker
    )

    var showTimePicker by remember { mutableStateOf(false) }
    var selectedHour by remember { mutableIntStateOf(0) }
    var selectedMinute by remember { mutableIntStateOf(0) }

    val timePickerState = rememberTimePickerState(
        initialHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
        initialMinute = Calendar.getInstance().get(Calendar.MINUTE),
        is24Hour = true
    )

    Title(R.string.choose_time)
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        Text(
            formatDate(value),
            style = MaterialTheme.typography.titleMedium,
        )

        Spacer(modifier = Modifier.weight(1f))
        ShowDatePickerButton {
            showDatePicker = true
        }
    }

    if (showDatePicker) ShowDatePicker(
        datePickerState = datePickerState,
        dismiss = { showDatePicker = false },
        confirm = {
            showDatePicker = false
            datePickerState.selectedDateMillis?.let {
                selectedDateMillis = it
                showTimePicker = true
            }
        }
    )
    if (showTimePicker) ShowTimePicker(
        timePickerState = timePickerState,
        dismiss = {
            showTimePicker = false
            showDatePicker = true
        },
        confirm = {
            selectedHour = timePickerState.hour
            selectedMinute = timePickerState.minute
            showTimePicker = false
            onValueChanged.invoke(
                combineDateAndTime(
                    selectedDateMillis,
                    selectedHour,
                    selectedMinute,
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTimePicker(
    timePickerState: TimePickerState,
    confirm: () -> Unit,
    dismiss: () -> Unit,
) {
    TimePickerDialog(
        onDismissRequest = dismiss,
        confirmButton = {
            TextButton(onClick = confirm) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = dismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        }
    ) {
        TimePicker(state = timePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    dismissButton: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column(
                modifier = Modifier.padding(
                    start = 24.dp,
                    top = 20.dp,
                    end = 24.dp,
                    bottom = 12.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .align(Alignment.Start)
                )
                content()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    dismissButton?.invoke()
                    Spacer(modifier = Modifier.width(8.dp))
                    confirmButton()
                }
            }
        }
    }
}

fun combineDateAndTime(dateMillis: Long, hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateMillis
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.timeInMillis
}

@Composable
fun ShowDatePickerButton(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Icon(
            Icons.Filled.DateRange,
            contentDescription = stringResource(R.string.choose_time)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowDatePicker(
    datePickerState: DatePickerState,
    dismiss: () -> Unit,
    confirm: () -> Unit,
) {
    DatePickerDialog(
        onDismissRequest = dismiss,
        confirmButton = {
            TextButton(
                onClick = confirm
            ) {
                Text(stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(
                onClick = dismiss
            ) {
                Text(stringResource(android.R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

fun formatDate(millis: Long): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = millis
    val sdf = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
    sdf.timeZone = java.util.TimeZone.getDefault()
    return sdf.format(calendar.time)
}