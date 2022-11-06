package com.mshdabiola.filemanager.data.model

data class RenameUiState(
    val show: Boolean = false,
    val currentName: String = "",
    val onNameChange: (String) -> Unit = {},
    val onDismissRequest: () -> Unit = {},
    val onRename: () -> Unit = {},
    val errorOccur: Boolean = false,
    val errorMsg: String = "",
    val extension: String = ".png"
)
