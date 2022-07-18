package com.mshdabiola.filemanager.home

data class HomeUiState (
    val memoryUiStates: List<MemoryUiState> = emptyList(),
    val homeRecentFiles: List<HomeRecentFile> = emptyList(),
    val categoryUiStates: List<CategoryUiState> = emptyList()
    )