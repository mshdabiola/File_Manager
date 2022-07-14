package com.mshdabiola.filemanager.data.model

import android.content.Context
import android.os.Environment
import dagger.hilt.android.scopes.ActivityScoped
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths
import javax.inject.Inject

@ActivityScoped
class FileSystem @Inject constructor(
    context: Context
) {
    val mainStorage = Environment.getExternalStorageDirectory()

    init {

    }
}