package com.mshdabiola.filemanager.data.model

data class MainUiState(
    val storageUiStates: List<StorageUiState> = emptyList(),
    val recentFiles: List<FileUiState> = emptyList(),
    val categoryUiStates: List<CategoryUiState> = emptyList(),
    val getAllRecentFileUistate: () -> Unit = {}
)