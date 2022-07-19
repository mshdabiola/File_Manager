package com.mshdabiola.filemanager.home

import android.os.Environment
import android.os.StatFs
import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.mshdabiola.filemanager.R
import com.mshdabiola.filemanager.formatSize
import java.io.File

class MemoryUiState (val name : String="Internal Storage",
                     @DrawableRes val icon : Int= R.drawable.ic_baseline_phone_android_24,
                     val path : String=""
                     ){
  var  totalSize : String="128 Gb"
   var freeSize : String="34 Gb"
   var usedSize : String="4 Gb"
    var fractionUsed= 0.6f
    val file = File(path)

    init {
      if (  file.exists()) {
          totalSize = getTotalMemorySize(file).formatSize()
          freeSize = getFreeMemorySize(file).formatSize()
          usedSize =getUsedMemorySize(file).formatSize()

          fractionUsed = getUsedMemorySize(file)/getTotalMemorySize(file).toFloat()

      }
      }



    private fun isExternalMemoryAvailable(): Boolean{
        return  Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
    }

    fun getFreeMemorySize(path : File):Long{
        return StatFs(path.path).freeBytes

    }
    fun getTotalMemorySize(path: File):Long{
        return StatFs(path.path).totalBytes

    }

    fun getUsedMemorySize(path: File):Long{
        return getTotalMemorySize(path)-getFreeMemorySize(path)
    }


}