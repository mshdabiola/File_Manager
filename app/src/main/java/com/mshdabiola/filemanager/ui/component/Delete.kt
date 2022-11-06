package com.mshdabiola.filemanager.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mshdabiola.filemanager.data.model.DeleteUiState

@Composable
fun DeleteDialog(deleteUiState: DeleteUiState, modifier: Modifier = Modifier) {

    AnimatedVisibility(modifier = modifier, visible = deleteUiState.show) {
        AlertDialog(
            onDismissRequest = { deleteUiState.onCancel() },
            title = { Text(text = "Delete File") },
            text = { Text(text = "Are you sure you want to permanently delete this file") },
            confirmButton = {
                FilledTonalButton(onClick = { deleteUiState.onDelete() }) {
                    Text(text = "Delete the file")
                }
            },
            dismissButton = {
                TextButton(onClick = { deleteUiState.onCancel() }) {
                    Text(text = "Cancel")
                }
            }
        )

    }

}

@Preview
@Composable
fun DeleteDialogPreview() {
    DeleteDialog(DeleteUiState(show = true))
}