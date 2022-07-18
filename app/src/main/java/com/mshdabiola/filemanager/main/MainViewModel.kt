package com.mshdabiola.filemanager.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.home.HomeUiState
import com.mshdabiola.filemanager.home.MemoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.R)
@HiltViewModel
class MainViewModel @Inject constructor(
    val savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
) : ViewModel(){
    val _uiState = MutableStateFlow(MainUiState("Main"))
    val uistate = _uiState.asStateFlow()

    val className = this::class.simpleName
    init {
        val askPermission = ContextCompat.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED
        _uiState.value = _uiState.value.copy(
            onPermissionRequest = this::onPermissionRequest,
            askPermission = askPermission,
            getAllFiles = this::getAllFiles
            )

       // getAllFiles(Environment.getExternalStorageDirectory().absolutePath)
    }

    private fun getAllFiles(string: String){

        val pathStr = if (string.isEmpty()) Environment.getExternalStorageDirectory().absolutePath else string
        Log.d(className,"$pathStr")

        val file = File(pathStr)
         val fileUiStates= file.listFiles()
             ?.map { FileUiState(name = it.name,isDirectory = it.isDirectory, path = it.toPath()) } ?: emptyList()

        _uiState.value =_uiState.value.copy(fileUiStateList = fileUiStates, name = if(string.isEmpty()) context.getString(R.string.app_name) else file.name)

    }

    private fun onPermissionRequest(isGrant : Boolean){

        _uiState.value = _uiState.value.copy(askPermission = !isGrant)
        if (isGrant){
            Log.d(className,"Granted")

        }else{
            Log.d(className,"denied")
        }

    }

    //home properties and function

    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()
    val storagePro = arrayOf("Internal Storage " to R.drawable.ic_baseline_phone_android_24,
        "External SD Card" to R.drawable.ic_baseline_sd_storage_24)

    init {
        val storages=  getExternalStorage(context)

      val memoryUiState=  storages.mapIndexed{index, path ->
          val pair= storagePro[index]
          MemoryUiState(name = pair.first, icon = pair.second,path=path ) }



        _homeUiState.value = _homeUiState.value.copy(memoryUiStates = memoryUiState)


        Environment.getStorageDirectory().listFiles()?.forEach {

            Log.d(className,it.absolutePath)
        }



    }

    private fun getExternalStorage(context: Context): List<String> {
        val storages = ContextCompat.getExternalFilesDirs(context,null)

        return storages.map { it.absolutePath.split("/Android").first() }



    }





}