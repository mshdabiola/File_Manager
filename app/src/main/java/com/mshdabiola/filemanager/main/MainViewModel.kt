package com.mshdabiola.filemanager.main

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle
) : ViewModel(){
    val _uiState = MutableStateFlow(MainUiState("Main"))
    val uistate = _uiState.asStateFlow()
}