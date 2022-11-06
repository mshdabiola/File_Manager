package com.mshdabiola.filemanager.extention

fun String.getGenericMimeType(): String {
    if (!contains("/"))
        return this

    val type = substring(0, indexOf("/"))
    return "$type/*"
}