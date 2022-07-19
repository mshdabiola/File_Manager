package com.mshdabiola.filemanager

fun Long.formatSize(): String{
    val size =this.toFloat()
    val kilobyte = 1024
    val mb = kilobyte*kilobyte
    val gb = mb*kilobyte

    var sizeInFloat =0f
    // val numberFormat = NumberFormat.getNumberInstance().format()
    val suffix = when{


        size >= gb -> {
            sizeInFloat = (size/gb)

            "Gb"
        }
        size >= mb -> {
            sizeInFloat = (size/mb)
            "Mb"
        }
        else ->{
            sizeInFloat = (size/kilobyte)
            "Kb"
        }
    }


    return String.format("%2.2f %s",sizeInFloat,suffix)
}