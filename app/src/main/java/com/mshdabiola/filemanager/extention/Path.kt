package com.mshdabiola.filemanager.extention

import android.graphics.BitmapFactory
import android.graphics.Point
import android.media.MediaMetadataRetriever
import android.os.StatFs
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.extension
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name


fun Path.isValidFileName(): Boolean {
    charArrayOf('/', '\n', '\r', '\t', '\u0000', '`', '?', '*', '\\', '<', '>', '|', '\"', ':')
        .forEach {
            if (name.contains(it))
                return false
        }
    return true
}

fun Path.isZipFile() = extension == "zip"

fun Path.getAvailableStorage(): Long {
    return StatFs(absolutePathString()).availableBytes
}

fun Path.getTotalStorage(): Long {
    return StatFs(absolutePathString()).totalBytes
}

fun Path.getUsedStorage(): Long {
    return getTotalStorage() - getAvailableStorage()
}

fun Path.getImageResolution(): Point? {
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeFile(absolutePathString(), options)
    val width = options.outWidth
    val height = options.outHeight
    return if (width > 0 && height > 0) {
        Point(options.outWidth, options.outHeight)
    } else {
        null
    }
}

fun Path.getFileSongTitle(): String? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(absolutePathString())
        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
    } catch (e: Exception) {
        return null
    }
}

fun Path.getFileDurationSeconds(): Long? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(absolutePathString())
        val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)

        time?.toLong()
    } catch (e: Exception) {
        null
    }
}

fun Path.getFileArtist(): String? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(absolutePathString())
        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
    } catch (ignored: Exception) {
        null
    }
}

fun Path.getFileAlbum(): String? {
    return try {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(absolutePathString())
        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)
    } catch (ignored: Exception) {
        null
    }
}

fun Path.isAudio() = Extention.MIME[extension]?.startsWith("audio") ?: false

fun Path.isImage() = Extention.MIME[extension]?.startsWith("image") ?: false
fun Path.isVideo() = Extention.MIME[extension]?.startsWith("video") ?: false

fun Path.isRaw() = Extention.rawExtensions.contains(extension)

fun Path.isSvg() = extension == "svg"

fun Path.isGif() = extension == "gif"

fun Path.isMediaFile() =
    isImage() || isVideo() || isGif() || isRaw() || isSvg()

fun Path.dk() {
    listDirectoryEntries()
}