package com.mshdabiola.filemanager.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mshdabiola.filemanager.data.model.RenameUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RenameDialog(
    modifier: Modifier = Modifier,
    renameUiState: RenameUiState = RenameUiState(
        show = true,
        errorMsg = "name show not contain *",
        errorOccur = true
    )
) {
    AnimatedVisibility(
        visible = renameUiState.show,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier

    ) {
        AlertDialog(
            onDismissRequest = { renameUiState.onDismissRequest() },
            title = {
                Text(
                    text = "Rename File",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = renameUiState.currentName,
                        onValueChange = { renameUiState.onNameChange(it) },

                        keyboardActions = KeyboardActions(onDone = {
                            renameUiState.onRename()
                            renameUiState.onDismissRequest()
                        }),
                        trailingIcon = {
                            Text(text = ".${renameUiState.extension}")
                        },

                        singleLine = true,
                        isError = renameUiState.errorOccur,

                        modifier = Modifier.fillMaxWidth()
                    )

                    if (renameUiState.errorOccur) {
                        Text(
                            text = renameUiState.errorMsg,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.paddingFromBaseline(top = 16.dp)
                        )
                    }

                }

            },
            dismissButton = {
                TextButton(onClick = {
                    renameUiState.onDismissRequest()

                }) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                FilledTonalButton(onClick = {
                    renameUiState.onRename()
                    renameUiState.onDismissRequest()
                }) {
                    Text(text = "Rename")
                }
            }

        )
    }

}

@Preview
@Composable
fun RenameDialogPreview() {
    RenameDialog()
}