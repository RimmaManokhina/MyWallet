package com.github.cawboyroy.mywallet.settings.presentation

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.github.cawboyroy.mywallet.R
import java.io.Serializable

interface ImportExportState : Serializable {

    @Composable
    fun Show(
        activity: Context,
        launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
        viewModel: ImportExportViewModel,
    )

    data object Initial : ImportExportState {
        private fun readResolve(): Any = Initial

        @Composable
        override fun Show(
            activity: Context,
            launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
            viewModel: ImportExportViewModel,
        ) {
            Button(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                onClick = viewModel::export
            ) {
                Text(stringResource(R.string.export))
            }

            Button(
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
        ) {
            Initial.Show(activity, launcher, viewModel)
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
        ) {
            Initial.Show(activity, launcher, viewModel)
            Text(
                textAlign = TextAlign.Center,
                color = LocalContentColor.current,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = stringResource(R.string.imported_successfully)
            )
        }
    }

    data class ImportInProgress(private val uri: Uri) : ImportExportState {
        private fun readResolve(): Any = ExportInProgress

        @Composable
        override fun Show(
            activity: Context,
            launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
            viewModel: ImportExportViewModel,
        ) {
            BackHandler { }
            CircularProgressIndicator()
            Text(
                color = LocalContentColor.current,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = stringResource(R.string.import_in_progress)
            )
            LaunchedEffect(uri) {
                viewModel.importInternal(uri)
            }
        }
    }

    data object ExportInProgress : ImportExportState {
        private fun readResolve(): Any = ExportInProgress

        @Composable
        override fun Show(
            activity: Context,
            launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
            viewModel: ImportExportViewModel,
        ) {
            BackHandler { }
            CircularProgressIndicator()
            Text(
                color = LocalContentColor.current,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                text = stringResource(R.string.export_in_progress)
            )
            LaunchedEffect(Unit) {
                viewModel.exportInternal()
            }
        }
    }

    data class Share(
        private val uri: String,
        private val timeId: Long = System.currentTimeMillis(),
    ) : ImportExportState {

        @Composable
        override fun Show(
            activity: Context,
            launcher: ManagedActivityResultLauncher<Array<String>, Uri?>,
            viewModel: ImportExportViewModel,
        ) {
            Initial.Show(activity, launcher, viewModel)
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