package com.jackom.loglib.formatter

/**
 * @author：jackom
 * @date：4/30/21 on 10:32 AM
 * @desc：日志转换接口
 */
interface ILogFormatter<T> {
    fun format(t: T): String
}