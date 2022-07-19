package com.mshdabiola.filemanager.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.home.CategoryUiState
import com.mshdabiola.filemanager.home.HomeRecentFile
import com.mshdabiola.filemanager.home.HomeUiState
import com.mshdabiola.filemanager.home.MemoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

             ?.sortedWith(compareBy<File> { it.isFile }.thenBy { it.name })
             ?.filter { it.isHidden.not() }
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
        val storages=  getExternalStorage()

      val memoryUiState=  storages.mapIndexed{index, path ->
          val pair= storagePro[index]
          MemoryUiState(name = pair.first, icon = pair.second,path=path ) }

        val categoryList = listOf(

            CategoryUiState(
                name = "Download",
                icon = R.drawable.ic_baseline_arrow_downward_24,
                path=getCategoryPath(Environment.DIRECTORY_DOWNLOADS)),

            CategoryUiState(
                name = "Document",
                icon = R.drawable.ic_baseline_text_snippet_24,
                path=getCategoryPath(Environment.DIRECTORY_DOCUMENTS)),

            CategoryUiState(
                name = "Picture",
                icon = R.drawable.ic_baseline_image_24,
                path=getCategoryPath(Environment.DIRECTORY_DCIM)),

            CategoryUiState(
                name = "Video",
                icon = R.drawable.ic_baseline_videocam_24,
                path=getCategoryPath(Environment.DIRECTORY_MOVIES)),

            CategoryUiState(
                name = "Music",
                icon = R.drawable.ic_baseline_music_note_24,
                path=getCategoryPath(Environment.DIRECTORY_MUSIC)))



        _homeUiState.value = _homeUiState.value.copy(
            memoryUiStates = memoryUiState,
            categoryUiStates = categoryList
            )

        getRecentFiles()

    }

    private fun getExternalStorage(): List<String> {
        val storages = ContextCompat.getExternalFilesDirs(context,null)

        return storages.map { it.absolutePath.split("/Android").first() }



    }

    private fun getCategoryPath(name : String) = Environment.getExternalStoragePublicDirectory(name).absolutePath

    private fun getRecentFiles(){
        viewModelScope.launch (Dispatchers.IO){

            val recentFile = getExternalStorage()
                .map { File(it).walkTopDown().maxDepth(2).toList() }
                .flatten()
                .filter { it.isFile && !it.isHidden }
                .onEach { Log.d(className,it.absolutePath) }
                .sortedWith( compareBy { -it.lastModified() })
                .take(10)
                .map { HomeRecentFile(name = it.name, path = it.absolutePath, modifiedDate = it.lastModified(), size = it.length()) }

                .toList()




            _homeUiState.value = _homeUiState.value.copy(homeRecentFiles = recentFile)
        }
    }



}