package com.mshdabiola.filemanager.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.R)
@HiltViewModel
class MainViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    val context: Context
) : ViewModel(){
    val _uiState = MutableStateFlow(MainUiState("Main"))
    val uistate = _uiState.asStateFlow()

    val className = this::class.simpleName
    init {
        val askPermission = ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED
        _uiState.value = _uiState.value.copy(onPermissionRequest = this::onPermissionRequest, askPermission = askPermission)
        getAllFiles(Environment.getDataDirectory())
        getAllFiles(Environment.getRootDirectory())
        getAllFiles(Environment.getStorageDirectory())
        getAllFiles(Environment.getExternalStorageDirectory())
        getAllFiles(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS))
    }

    private fun getAllFiles(fileS : File){


       val file= fileS
            .listFiles()
            ?.mapNotNull { it }

      val fileString= file?.joinToString(separator = ",\n") { file -> file.name }
        Log.d(className,fileS.absolutePath)
        Log.d(className,fileString ?: "no files")


    }



    private fun onPermissionRequest(isGrant : Boolean){

        _uiState.value = _uiState.value.copy(askPermission = !isGrant)
        if (isGrant){
            Log.d(className,"Granted")

        }else{
            Log.d(className,"denied")
        }

    }
}