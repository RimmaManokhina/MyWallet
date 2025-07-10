package com.github.cawboyroy.mywallet.settings.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.add.presentation.Title
import java.io.Serializable

interface ImportExportState : Serializable {

    @Composable
    fun Show(
        activity: Context,
        launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
        viewModel: ImportExportViewModel,
        userInput: String,
        onUserInputChange: (String) -> Unit,
    )

    data object Initial : ImportExportState {
        private fun readResolve(): Any = Initial

        @Composable
        override fun Show(
            activity: Context,
            launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
            viewModel: ImportExportViewModel,
            userInput: String,
            onUserInputChange: (String) -> Unit,
        ) {
            Title(R.string.key_password)
            BasicTextField(
                cursorBrush = SolidColor(LocalContentColor.current),
                textStyle = TextStyle(fontSize = 18.sp, color = LocalContentColor.current),
                value = userInput,
                onValueChange = onUserInputChange,
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
            Button(
                enabled = userInput.isNotEmpty(),
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                onClick = {
                    viewModel.export(userInput)
                }
            ) {
                Text(stringResource(R.string.export))
            }

            Button(
                enabled = userInput.isNotEmpty(),
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                onClick = {
                    launcher.launch(arrayOf("application/json"))
                }
            ) {
                Text(stringResource(R.string.importing))
            }
        }
    }

    data object Failed : ImportExportState {
        private fun readResolve(): Any = Failed

        @Composable
        override fun Show(
            activity: Context,
            launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
            viewModel: ImportExportViewModel,
            userInput: String,
            onUserInputChange: (String) -> Unit,
        ) {
            Initial.Show(activity, launcher, viewModel, userInput, onUserInputChange)
            Text(
                textAlign = TextAlign.Center,
                color = Color.Red,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = stringResource(R.string.failed_to_import)
            )
        }
    }

    data object Imported : ImportExportState {
        private fun readResolve(): Any = Imported

        @Composable
        override fun Show(
            activity: Context,
            launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
            viewModel: ImportExportViewModel,
            userInput: String,
            onUserInputChange: (String) -> Unit,
        ) {
            onUserInputChange("")
            Initial.Show(activity, launcher, viewModel, userInput, onUserInputChange)
            Text(
                textAlign = TextAlign.Center,
                color = LocalContentColor.current,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = stringResource(R.string.imported_successfully)
            )
        }
    }

    data class ImportInProgress(private val uri: Uri, private val password: String) :
        ImportExportState {

        @Composable
        override fun Show(
            activity: Context,
            launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
            viewModel: ImportExportViewModel,
            userInput: String,
            onUserInputChange: (String) -> Unit,
        ) {
            BackHandler { }
            CircularProgressIndicator()
            Text(
                color = LocalContentColor.current,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = stringResource(R.string.import_in_progress)
            )
            LaunchedEffect(uri) {
                viewModel.importInternal(uri, password)
            }
        }
    }

    data class ExportInProgress(private val password: String) : ImportExportState {

        @Composable
        override fun Show(
            activity: Context,
            launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
            viewModel: ImportExportViewModel,
            userInput: String,
            onUserInputChange: (String) -> Unit,
        ) {
            BackHandler { }
            CircularProgressIndicator()
            Text(
                color = LocalContentColor.current,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = stringResource(R.string.export_in_progress)
            )
            LaunchedEffect(Unit) {
                viewModel.exportInternal(password)
            }
        }
    }

    data class Share(
        private val uri: String,
        private val timeId: Long = System.currentTimeMillis()
    ) : ImportExportState {

        @Composable
        override fun Show(
            activity: Context,
            launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
            viewModel: ImportExportViewModel,
            userInput: String,
            onUserInputChange: (String) -> Unit,
        ) {
            onUserInputChange("")
            Initial.Show(activity, launcher, viewModel, userInput, onUserInputChange)
            LaunchedEffect(timeId) {
                val intent = Intent(Intent.ACTION_SEND)
                    .setType("application/json")
                    .putExtra(Intent.EXTRA_STREAM, uri.toUri())
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                activity.startActivity(
                    Intent.createChooser(
                        intent,
                        activity.getString(R.string.send_file)
                    )
                )
            }
        }
    }
}