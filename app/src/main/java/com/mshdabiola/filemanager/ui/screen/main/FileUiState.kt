package com.mshdabiola.filemanager.ui.screen.main

import android.text.format.DateFormat
import java.io.File
import java.nio.file.Path
import java.util.*
import kotlin.io.path.Path

data class FileUiState(val name : String="File name",
                       val isDirectory : Boolean = false ,
                       val path : String ="",
                       val size : Long =8483L,
                       val lastModified : Long = Date().time
                       ){
    var isSelected = false
    var numOfItems = 4
    val date = DateFormat.format("hh/mm dd-MM-yyyy",Date(lastModified))

    init {
        val file = File(path)

        numOfItems = file.listFiles()?.size ?: 0

    }
}