package com.jackom.utilslib

import java.io.Closeable

/**
 * @author : zhengminxin
 * @date : 2/2/2021 2:53 PM
 * @desc :
 */
object IOUtils {

    fun close(x: Closeable?) {
        if (x != null) {
            try {
                x.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}