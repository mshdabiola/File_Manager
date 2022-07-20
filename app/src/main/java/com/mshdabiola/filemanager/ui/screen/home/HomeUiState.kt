package com.mshdabiola.filemanager.ui.screen.home

data class HomeUiState (
    val memoryUiStates: List<MemoryUiState> = emptyList(),
    val homeRecentFiles: List<HomeRecentFile> = emptyList(),
    val categoryUiStates: List<CategoryUiState> = emptyList()
    )