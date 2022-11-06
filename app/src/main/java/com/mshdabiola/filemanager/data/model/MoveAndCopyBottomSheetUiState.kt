package com.mshdabiola.filemanager.data.model

data class MoveAndCopyBottomSheetUiState(
    val show: Boolean = false,
    val actionName: String = "Copy file to",
    val onDismiss: () -> Unit = {},
    val listOfStorageUiState: List<StorageUiState> = listOf(
        StorageUiState(), StorageUiState()
    )
)
