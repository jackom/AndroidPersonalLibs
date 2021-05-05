package com.jackom.loglib.printer

import android.util.Log
import com.jackom.loglib.LogConfigAbstract
import com.jackom.loglib.LogPriority

/**
 * @author：jackom
 * @date：5/1/21 on 8:58 AM
 * @desc：
 */
class LogConsolePrinter: ILogPrinter {

    override fun printLogs(@LogPriority.Priority priority: Int, tag: String, contents: String) {
        val len = contents.length
        val countOfLine = len / LogConfigAbstract.MAX_LINE_LEN
        if (countOfLine > 0) {
            var index = 0
            for (i in 0 until countOfLine) {
                Log.println(priority, tag, contents.substring(index, index + LogConfigAbstract.MAX_LINE_LEN))
                index += LogConfigAbstract.MAX_LINE_LEN
            }
            if (index != len) {
                Log.println(priority, tag, contents.substring(index, len))
            }
        } else {
            Log.println(priority, tag, contents)
        }

//        Log.println(priority, tag, contents)
    }

    override fun isSupport(): Boolean = true

}