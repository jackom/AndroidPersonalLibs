package com.jackom.loglib

/**
 * @author：jackom
 * @date：4/30/21 on 5:58 PM
 * @desc：过滤工具类
 */
object LogStackTraceUtils {

    /**
     * 获取裁剪后且除忽略包之外的堆栈信息
     */
    fun getCroppedRealStackTrace(stackTraceElements: Array<StackTraceElement>, ignorePkg: String?, maxDepth: Int): Array<StackTraceElement> {
        return cropStackTrace(getRealStackTrace(stackTraceElements, ignorePkg), maxDepth)
    }

    /**
     * 获取除忽略包之外的堆栈信息
     */
    private fun getRealStackTrace(stackTraceElements: Array<StackTraceElement>, ignorePkg: String?): Array<StackTraceElement> {
        val originDepth = stackTraceElements.size
        var ignoreDepth = 0
        for (i in originDepth - 1 downTo 0) {
            val className = stackTraceElements[i].className
            if (null != ignorePkg && className.startsWith(ignorePkg)) {
                ignoreDepth = i + 1
                break
            }
        }
        val resultDepth = originDepth - ignoreDepth
        val realStackTraceElements = arrayOfNulls<StackTraceElement>(resultDepth) as Array<StackTraceElement>
        System.arraycopy(stackTraceElements, ignoreDepth, realStackTraceElements, 0, resultDepth)
        return realStackTraceElements
    }

    /**
     * 裁剪堆栈信息
     */
    private fun cropStackTrace(stackTraceElements: Array<StackTraceElement>, maxDepth: Int): Array<StackTraceElement> {
        var realDepth = stackTraceElements.size
        if (maxDepth > 0) {
            //取最小值
            realDepth = realDepth.coerceAtMost(maxDepth)
        }
        val realStackTraceElements = arrayOfNulls<StackTraceElement>(realDepth) as Array<StackTraceElement>
        System.arraycopy(stackTraceElements, 0, realStackTraceElements, 0, realDepth)
        return realStackTraceElements
    }

}