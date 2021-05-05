package com.jackom.loglib

/**
 * @author：jackom
 * @date：4/30/21 on 10:30 AM
 * @desc：日志配置
 */
abstract class LogConfigAbstract {

    /**
     * 是否打开log日志
     */
    open fun isLogOpen(): Boolean = true

    /**
     * 全局Tag名称配置
     */
    open fun globalTag(): String = "LogConfigs"

    /**
     * 打印栈的深度
     */
    open fun stackTraceDepth(): Int = DEFAULT_VISIBLE_STACK_SIZE

    /**
     * 是否打印当前线程信息
     */
    open fun isPrintCurThreadInfos(): Boolean = true

    open fun injectJsonParser(): JsonParser? = null

    /**
     * json解析接口，方便调用者用不同的方式(fastjson or gson)去解析json
     */
    interface JsonParser {
        fun parse2Json(obj: Any): String
    }

    companion object {
        /**
         * 默认可显示堆栈大小
         */
        const val DEFAULT_VISIBLE_STACK_SIZE: Int = 5

        const val MAX_LINE_LEN = 512
    }

}