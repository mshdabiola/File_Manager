package com.mshdabiola.filemanager.data.model

import java.nio.file.Path

data class SelectUiState(
    val folderName: String = "Internal Storage",
    val listOfFileUiState: List<FileUiState> = emptyList(),
    val currentPath: Path? = null,
    val state: CopyMoveEnum = CopyMoveEnum.COPY,
    val getAllDirectory: (String) -> Unit = {},
    val onSelectClick: (String) -> Unit = {},
    val addNewFolder: (String) -> Unit = {}
)
