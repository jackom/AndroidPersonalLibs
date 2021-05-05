package com.jackom.loglib

import androidx.annotation.NonNull
import java.lang.StringBuilder

/**
 * @author：jackom
 * @date：4/30/21 on 10:37 AM
 * @desc：log门面，提供调用方式
 */
object LogFacade {

    private val TAG: String = LogFacade.javaClass.name
    private val IGNORE_PKG_PREFIX = TAG.substring(0, TAG.lastIndexOf(".") + 1)

    fun log4V(vararg args: Any) {
        log4V(LogManager.instance.logConfig.globalTag(), *args)
    }

    fun log4V(tag: String, vararg args: Any) {
        log(LogManager.instance.logConfig, LogPriority.V, tag, *args)
    }

    fun log4D(vararg args: Any) {
        log4D(LogManager.instance.logConfig.globalTag(), *args)
    }

    fun log4D(tag: String, vararg args: Any) {
        log(LogManager.instance.logConfig, LogPriority.D, tag, *args)
    }

    fun log4I(vararg args: Any) {
        log4I(LogManager.instance.logConfig.globalTag(), *args)
    }

    fun log4I(tag: String, vararg args: Any) {
        log(LogManager.instance.logConfig, LogPriority.I, tag, *args)
    }

    fun log4W(vararg args: Any) {
        log4W(LogManager.instance.logConfig.globalTag(), *args)
    }

    fun log4W(tag: String, vararg args: Any) {
        log(LogManager.instance.logConfig, LogPriority.W, tag, *args)
    }

    fun log4E(vararg args: Any) {
        log4E(LogManager.instance.logConfig.globalTag(), *args)
    }

    fun log4E(tag: String, vararg args: Any) {
        log(LogManager.instance.logConfig, LogPriority.E, tag, *args)
    }

    fun log4A(vararg args: Any) {
        log4A(LogManager.instance.logConfig.globalTag(), *args)
    }

    fun log4A(tag: String, vararg args: Any) {
        log(LogManager.instance.logConfig, LogPriority.A, tag, *args)
    }

    fun log(logConfig: LogConfigAbstract, @LogPriority.Priority priority: Int, tag: String, vararg args: Any) {

        if (!logConfig.isLogOpen()) {
            return
        }

        val sb = StringBuilder()
        if (logConfig.isPrintCurThreadInfos()) {
            //打印当前线程信息
            sb.append(LogManager.instance.THREAD_FORMATTER.format(Thread.currentThread())).append("\n")
        }

        if (logConfig.stackTraceDepth() > 0) {
            //打印堆栈信息
            sb.append(LogManager.instance.STACK_TRACE_FORMATTER.format(LogStackTraceUtils.getCroppedRealStackTrace(Throwable().stackTrace
                    , IGNORE_PKG_PREFIX, logConfig.stackTraceDepth()))).append("\n")
        }

        val msg = parseDatas(logConfig, *args)
        sb.append(msg)
        val contents = sb.toString()

        val printers = LogManager.instance.getPrinters()
        printers.forEach { printer ->
            if (printer.isSupport()) {
                printer.printLogs(priority, tag, contents)
            }
        }

    }

    private fun parseDatas(logConfig: LogConfigAbstract, @NonNull vararg args: Any): String {

        if (logConfig.injectJsonParser() != null) {
            return logConfig.injectJsonParser()!!.parse2Json(args)
        }

        val sb = StringBuilder()
        for ((index, value) in args.withIndex()) {
            sb.append(value.toString()).append(";")
            if (index == args.size - 1) {
                sb.deleteCharAt(index)
            }
        }
        return sb.toString()
    }

}