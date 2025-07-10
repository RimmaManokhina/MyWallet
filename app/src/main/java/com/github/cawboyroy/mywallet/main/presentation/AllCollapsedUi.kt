package com.github.cawboyroy.mywallet.main.presentation

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.cawboyroy.mywallet.R
import java.io.Serializable

interface AllCollapsedUi : Serializable {

    @Composable
    fun Show(actions: AllDayActions)

    abstract class Abstract(
        @StringRes private val description: Int,
    ) : AllCollapsedUi {

        protected abstract val icon: ImageVector

        protected abstract fun onClick(actions: AllDayActions)

        @Composable
        override fun Show(actions: AllDayActions) = Button(
            modifier = Modifier.padding(8.dp),
            onClick = { onClick(actions) }
        ) {
            Icon(
                icon,
                contentDescription = stringResource(description)
            )
        }
    }

    data object Collapsed : Abstract(R.string.expand) {
        private fun readResolve(): Any = Collapsed
        override val icon = Icons.Filled.KeyboardArrowDown
        override fun onClick(actions: AllDayActions) = actions.expandAll()
    }

    data object Expanded : Abstract(R.string.collapse) {
        private fun readResolve(): Any = Collapsed
        override val icon = Icons.Filled.KeyboardArrowUp
        override fun onClick(actions: AllDayActions) = actions.collapseAll()
    }
}

interface AllDayActions {

    fun expandAll()

    fun collapseAll()
}