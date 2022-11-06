package com.mshdabiola.filemanager.data.model

import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.extention.Extention
import com.mshdabiola.filemanager.extention.formatDate
import com.mshdabiola.filemanager.formatSize
import java.nio.file.Path
import java.util.*
import kotlin.io.path.*

data class FileUiState(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    val path: Path = Path(""),
    val isSelected: Boolean = false,
    val onSelectedClick: (Long) -> Unit = {},
    val onMoreClicked: (Long) -> Unit = {},
    val onClicked: (Long) -> Unit = {}
) {

    val name
        get() = path.name

    val isDirectory
        get() = path.isDirectory()

    val size
        get() = path.fileSize().formatSize()

    val lastModified
        get() = path.getLastModifiedTime().toMillis().formatDate()

    val drawableRes
        get() = if (isDirectory) R.drawable.ic_baseline_folder_24
        else Extention.placeHolder[path.extension] ?: R.drawable.ic_file_generic


    val numberOfItems
        get() = if (isDirectory) {
            try {
                path.listDirectoryEntries().size
            } catch (e: Exception) {
                " "
            }

        } else "45"


}