package com.mshdabiola.filemanager.main

data class MainUiState (
    val name : String,
    val onPermissionRequest : (Boolean)->Unit ={},
    val askPermission :Boolean = false

)