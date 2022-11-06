package com.mshdabiola.filemanager.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Environment
import androidx.core.content.ContextCompat
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.data.model.CategoryUiState
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.*

class FileRepository @Inject constructor(@ApplicationContext val context: Context) {


    val listOfCategory = listOf(

        CategoryUiState(
            name = "Download",
            icon = R.drawable.ic_baseline_arrow_downward_24,
            path = getCategoryPath(Environment.DIRECTORY_DOWNLOADS)
        ),

        CategoryUiState(
            name = "Document",
            icon = R.drawable.ic_baseline_text_snippet_24,
            path = getCategoryPath(Environment.DIRECTORY_DOCUMENTS)
        ),

        CategoryUiState(
            name = "Picture",
            icon = R.drawable.ic_baseline_image_24,
            path = getCategoryPath(Environment.DIRECTORY_DCIM)
        ),

        CategoryUiState(
            name = "Video",
            icon = R.drawable.ic_baseline_videocam_24,
            path = getCategoryPath(Environment.DIRECTORY_MOVIES)
        ),

        CategoryUiState(
            name = "Music",
            icon = R.drawable.ic_baseline_music_note_24,
            path = getCategoryPath(Environment.DIRECTORY_MUSIC)
        )
    )

    fun getStorage(): List<Path> {
        return getExternalStorage().map { Path(it) }
    }

    fun getAllFileInDirectory(pathstr: String): List<Path> {
        return Path(pathstr).listDirectoryEntries()

            .sortedWith(compareBy<Path> { !it.isDirectory() }.thenBy { it.name })
            .filter { it.isHidden().not() }


    }

    fun getRecentFiles(): List<Path> {


        return getExternalStorage()
            .map { File(it).walkTopDown().maxDepth(2).toList() }
            .flatten()
            .filter { it.isFile && !it.isHidden }
            .sortedWith(compareBy { -it.lastModified() })
            .take(10)
            .map { it.toPath() }
            .toList()

    }

    fun getCategories() = listOfCategory

    private fun getExternalStorage(): List<String> {
        val storages = ContextCompat.getExternalFilesDirs(context, null)
        return storages.map { it.absolutePath.split("/Android").first() }

    }

    fun isPermissionAccepted(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getCategoryPath(path: String) =
        Environment.getExternalStoragePublicDirectory(path).absolutePath

}