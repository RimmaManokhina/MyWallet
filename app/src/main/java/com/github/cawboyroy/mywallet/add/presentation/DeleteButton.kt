package com.github.cawboyroy.mywallet.add.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cawboyroy.mywallet.R
import java.io.Serializable

@Composable
fun DeleteButton(onDelete: () -> Unit) {
    var state: DeleteState by rememberSaveable { mutableStateOf(DeleteState.Initial) }
    Button(
        modifier = Modifier.padding(start = 16.dp),
        onClick = {
            state.onClick(onDelete) {
                state = it
            }
        }
    ) {
        state.Show()
    }
}

interface DeleteState : Serializable {

    fun onClick(onDelete: () -> Unit, update: (DeleteState) -> Unit)

    @Composable
    fun Show()

    data object Initial : DeleteState {
        private fun readResolve(): Any = Confirm

        override fun onClick(
            onDelete: () -> Unit,
            update: (DeleteState) -> Unit
        ) {
            update.invoke(Confirm)
        }

        @Composable
        override fun Show() {
            Icon(
                Icons.Filled.Delete,
                contentDescription = stringResource(R.string.delete)
            )
        }
    }

    data object Confirm : DeleteState {
        private fun readResolve(): Any = Confirm

        override fun onClick(
            onDelete: () -> Unit,
            update: (DeleteState) -> Unit
        ) {
            onDelete.invoke()
        }

        @Composable
        override fun Show() {
            Text(stringResource(R.string.confirm_delete))
        }
    }
}