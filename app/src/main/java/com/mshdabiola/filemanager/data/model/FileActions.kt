package com.mshdabiola.filemanager.data.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import com.mshdabiola.filemanager.extention.Extention
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.nio.file.Path
import javax.inject.Inject
import kotlin.io.path.extension

class FileActions @Inject constructor(@ApplicationContext val context: Context) {

    fun openFile(file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri, Extention.MIME[file.extension])
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    fun shareFile(listOfPath: List<Path>) {
        if (listOfPath.size == 1) {
            val path = listOfPath[0]
            val intent = ShareCompat.IntentBuilder(context)
                .setChooserTitle("Share File to")
                .setType(Extention.MIME.getOrDefault(path.extension, "*/*"))
                .setStream(toUri(path.toFile()))

                .intent

            val chooser = Intent.createChooser(intent, "Share my File to")
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)

        }


    }

    fun toUri(file: File): Uri {
        return FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            file
        )
    }
}

