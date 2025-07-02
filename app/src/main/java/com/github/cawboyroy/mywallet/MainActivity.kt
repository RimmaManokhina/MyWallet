package com.github.cawboyroy.mywallet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.cawboyroy.mywallet.add.presentation.AddExpensesViewModel
import com.github.cawboyroy.mywallet.add.presentation.ListViewModel
import com.github.cawboyroy.mywallet.ui.theme.MyWalletTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyWalletTheme {
                var showBottomSheet by remember { mutableStateOf(false) }
                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { showBottomSheet = true }
                        ) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = stringResource(R.string.add)
                            )
                        }
                    }
                ) { contentPadding ->
                    ListContent(contentPadding)

                    if (showBottomSheet) //todo replace by navController
                        AddExpenses { showBottomSheet = false }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenses(
    viewModel: AddExpensesViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
) {

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        var text by remember { mutableStateOf("") }
        BasicTextField(
            value = text,
            onValueChange = { text = it },
            modifier = Modifier
                .padding(all = 16.dp)
                .fillMaxWidth()
                .height(100.dp)
                .border(1.dp, Color.Gray)
                .padding(8.dp),
            singleLine = false,
            minLines = 2,
            maxLines = 4
        )

        Button(
            enabled = text.isNotEmpty(),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.End),
            onClick = {
                viewModel.add(text)
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        onDismiss()
                    }
                }
            }
        ) {
            Text(stringResource(R.string.save))
        }
    }
}


@Composable
fun ListContent(paddingValues: PaddingValues) {
    val viewModel: ListViewModel = hiltViewModel()
    val data = viewModel.state.collectAsStateWithLifecycle(emptyList()).value

    LazyColumn {
        items(data.size) {
            Text(text = data[it])
        }
    }
}