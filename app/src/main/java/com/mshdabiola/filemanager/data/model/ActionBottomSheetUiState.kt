package com.mshdabiola.filemanager.data.model

data class ActionBottomSheetUiState(
    val show: Boolean = false,
    val onDismiss: () -> Unit = {},
    val onSelect: () -> Unit = {},
    val onCopy: () -> Unit = {},
    val onMove: () -> Unit = {},
    val onShare: () -> Unit = {},
    val onRename: () -> Unit = {},
    val onDelete: () -> Unit = {},
    val onOpenWith: () -> Unit = {}


)
