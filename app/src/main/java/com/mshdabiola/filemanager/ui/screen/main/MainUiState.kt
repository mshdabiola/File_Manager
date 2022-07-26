package com.mshdabiola.filemanager.ui.screen.main

data class MainUiState (
    val name : String,
    val onPermissionRequest : (Boolean)->Unit ={},
    val askPermission :Boolean = false,
    val fileUiStateList: List<FileUiState> = emptyList(),
    val getAllFiles : (String)->Unit = {}
)