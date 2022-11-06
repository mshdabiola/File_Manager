package com.mshdabiola.filemanager.extention

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import java.nio.file.Path


fun Context.openUri(uri: String) {
    Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(uri)
    }.also {
        startActivity(it)
    }
}

val Context.version: String
    get() = packageManager.getPackageInfo(this.packageName, 0).versionName

fun Context.getUriFromPath(path: Path): Uri =
    FileProvider.getUriForFile(this, AUTHORITY, path.toFile())