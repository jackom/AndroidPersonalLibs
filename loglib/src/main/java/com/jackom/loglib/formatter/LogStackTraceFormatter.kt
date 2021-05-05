package com.jackom.loglib.formatter


/**
 * @author：jackom
 * @date：4/30/21 on 5:03 PM
 * @desc：转换stackTrace相关的log日志
 */
class LogStackTraceFormatter: ILogFormatter<Array<StackTraceElement>> {

    override fun format(stackTraceElements: Array<StackTraceElement>): String {

        if (stackTraceElements.isNullOrEmpty()) {
            return "empty stackTraceElements!"
        }

        val sb = StringBuilder("\t |-")
        for (value in stackTraceElements) {
            sb.append("\n\t\t").append(value.toString())
        }
        sb.append("\n\t -|")

        return sb.toString()
    }

}