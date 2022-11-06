package com.mshdabiola.filemanager.data.model

data class DeleteUiState(
    val show: Boolean = false,
    val onCancel: () -> Unit = {},
    val onDelete: () -> Unit = {}
)
