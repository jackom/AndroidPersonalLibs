package com.jackom.loglib

import com.jackom.loglib.formatter.LogStackTraceFormatter
import com.jackom.loglib.formatter.LogThreadFormatter
import com.jackom.loglib.printer.ILogPrinter

/**
 * @author：jackom
 * @date：4/30/21 on 10:31 AM
 * @desc：日志管理
 */
class LogManager {

    val THREAD_FORMATTER: LogThreadFormatter by lazy {
        LogThreadFormatter()
    }

    val STACK_TRACE_FORMATTER: LogStackTraceFormatter by lazy {
        LogStackTraceFormatter()
    }

    private val printers: MutableList<ILogPrinter> = mutableListOf()

    fun addPrinter(printer: ILogPrinter) {
        printers.add(printer)
    }

    fun removePrinter(printer: ILogPrinter) {
        printers.remove(printer)
    }

    fun getPrinters(): MutableList<ILogPrinter> {
        return printers
    }

    fun initLogConfig(logConfig: LogConfigAbstract, vararg args: ILogPrinter) {
        this.logConfig = logConfig
        printers.addAll(args)
    }

    lateinit var logConfig: LogConfigAbstract

    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder = LogManager()
    }

}