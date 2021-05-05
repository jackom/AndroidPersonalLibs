package com.jackom.loglib.formatter

/**
 * @author：jackom
 * @date：4/30/21 on 5:01 PM
 * @desc：转换线程相关的log日志
 */
class LogThreadFormatter: ILogFormatter<Thread> {

    override fun format(thread: Thread): String {
        return "current_Thread: ${thread.name}"
    }

}