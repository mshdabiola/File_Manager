package com.mshdabiola.filemanager.main

import java.nio.file.Path
import kotlin.io.path.Path

data class FileUiState(val name : String="File name",

                       val isDirectory : Boolean = false ,
                       val path : Path = Path("")
                       )