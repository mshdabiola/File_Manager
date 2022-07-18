package com.mshdabiola.filemanager

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun formatSize(){
        assertEquals(String.format("%10d",43433),formatSize(25772929024))
    }
    private fun formatSize(size : Long ): String{
        val kilobyte = 1024
        var sizeInFloat =0f

        // val numberFormat = NumberFormat.getNumberInstance().format()
        val surfix = when(divide(size)){


            2 -> {
                sizeInFloat = size/(1024f*1024f)

                "Mb"
            }
            3 -> {
                sizeInFloat = size/(1024f*1024f*1024f)
                "Gb"
            }
            else ->{
                sizeInFloat = size/1024f
                "Kb"
            }
        }



        return String.format("%2.2f %s",sizeInFloat,surfix)
    }

    private fun divide(size: Long) : Int{
        return if (size< 1024)
            0
        else
            divide(size/1024) +1
    }
}