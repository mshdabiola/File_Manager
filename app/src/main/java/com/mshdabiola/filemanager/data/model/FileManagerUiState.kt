package com.mshdabiola.filemanager.data.model

data class FileManagerUiState(
    val name: String,
    val fileUiStateList: List<FileUiState> = emptyList(),
    val actionBottomSheetUiState: ActionBottomSheetUiState = ActionBottomSheetUiState(),
    val moveAndCopyBottomSheetUiState: MoveAndCopyBottomSheetUiState = MoveAndCopyBottomSheetUiState(),
    val renameUiState: RenameUiState = RenameUiState(),
    val deleteUiState: DeleteUiState = DeleteUiState(),
    val currentFileUiState: FileUiState = FileUiState(),
    val isInSelectedMode: Boolean = false,
    val onDeselectedAllFile: () -> Unit = {},
    val onSelectedAllFile: () -> Unit = {},
    val openActionBottomSheet: () -> Unit = {},
    val getAllFiles: (String) -> Unit = {}

)