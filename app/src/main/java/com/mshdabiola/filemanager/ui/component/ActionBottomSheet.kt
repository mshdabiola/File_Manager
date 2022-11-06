package com.mshdabiola.filemanager.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.data.model.ActionBottomSheetUiState

@Composable
fun ActionBottomSheet(
    modifier: Modifier = Modifier,
    actionBottomSheetUiState: ActionBottomSheetUiState = ActionBottomSheetUiState(
        show = true
    ),
    navigateToFileInfo: () -> Unit = {},
    isDirectory: Boolean = false,
    numOfItemSelected: Int = 0,
    isSelectMode: Boolean = false,
    isDirectorySelected: Boolean = false
) {
    BottomSheet(
        modifier = modifier,
        actionBottomSheetUiState.show,
        onDismissRequest = actionBottomSheetUiState.onDismiss
    ) {
        Column {
            if (numOfItemSelected < 1) {
                DropdownMenuItem(
                    text = { Text(text = "Select") },
                    onClick = {
                        actionBottomSheetUiState.onSelect()
                        actionBottomSheetUiState.onDismiss()

                    },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_selected_24),
                            contentDescription = ""
                        )
                    }
                )
            }

            DropdownMenuItem(
                text = { Text(text = "Copy to") },
                onClick = {
                    actionBottomSheetUiState.onCopy()
                    actionBottomSheetUiState.onDismiss()
                },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_content_copy_24),
                        contentDescription = ""
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = "Move to") },
                onClick = {
                    actionBottomSheetUiState.onMove()
                    actionBottomSheetUiState.onDismiss()

                },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_move_24),
                        contentDescription = ""
                    )
                }
            )
            if (numOfItemSelected < 2) {
                DropdownMenuItem(
                    text = { Text(text = "Rename") },
                    onClick = {
                        actionBottomSheetUiState.onRename()
                        actionBottomSheetUiState.onDismiss()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_rename_24),
                            contentDescription = ""
                        )
                    }
                )
            }
            if (!isDirectory && numOfItemSelected < 2 && !isDirectorySelected) {
                DropdownMenuItem(
                    text = { Text(text = "share") },
                    onClick = {
                        actionBottomSheetUiState.onShare()
                        actionBottomSheetUiState.onDismiss()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = ""
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Open in another app") },
                    onClick = {
                        actionBottomSheetUiState.onOpenWith()
                        actionBottomSheetUiState.onDismiss()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_open_in_new_24),
                            contentDescription = ""
                        )
                    }
                )
            }
            DropdownMenuItem(
                text = { Text(text = "Delete") },
                onClick = {

                    actionBottomSheetUiState.onDelete()
                    actionBottomSheetUiState.onDismiss()
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = ""
                    )
                }
            )
            if (numOfItemSelected < 2) {
                DropdownMenuItem(
                    text = { Text(text = "${if (isDirectory || isDirectorySelected) "Folder" else "File"} Info") },
                    onClick = {

                        navigateToFileInfo()
                        actionBottomSheetUiState.onDismiss()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = ""
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun ActionBottomPreview() {
    ActionBottomSheet()

}