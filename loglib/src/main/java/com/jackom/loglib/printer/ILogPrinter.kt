package com.jackom.loglib.printer

import com.jackom.loglib.LogPriority

/**
 * @author：jackom
 * @date：4/30/21 on 10:33 AM
 * @desc：日志打印接口
 */
interface ILogPrinter {

    fun printLogs(@LogPriority.Priority priority: Int, tag: String, contents: String)

    /**
     * 是否支持
     */
    fun isSupport(): Boolean = true
}