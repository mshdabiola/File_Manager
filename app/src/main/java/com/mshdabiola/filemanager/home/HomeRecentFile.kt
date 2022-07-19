package com.mshdabiola.filemanager.home

import android.text.format.DateFormat
import com.mshdabiola.filemanager.formatSize
import java.util.*
import java.util.logging.SimpleFormatter

data class HomeRecentFile(val name : String="File",val path : String ="",val size : Long =1038,val modifiedDate : Long =GregorianCalendar().timeInMillis){


    val date = DateFormat.format("hh-mm-ss dd-MM-yyyy",Date(modifiedDate))
    val sizeStr : String
    get() = size.formatSize()
}
