package com.jackom.utilslib

import android.util.Log
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 *  author : zhengminxin
 *  date : 2019/7/1 12:00
 *  desc : Log工具类，link：https://blog.csdn.net/xiayiye5/article/details/80756422
 * 日志相关类:默认是测试环境<br>
 * <b>支持：存储Log日志文件到本地。发送Log日志信息到服务器</b>
 */
object LogUtils {

    private val isDebug = BuildConfig.DEBUG

    private val TAG = "LogUtils"

    private val LINE_SEPARATOR = System.getProperty("line.separator")


    @JvmStatic
    fun http(tag: String,msg: String?) {
        if (isDebug) {
            if(msg!=null){
                Log.d(tag, msg)
            }
        }
    }

    @JvmStatic
    fun v(msg: String?) {
        if (isDebug) {
            Log.v(TAG, getMsgFormat(msg))
        }
    }

    @JvmStatic
    fun v(tag: String, msg: String?) {
        if (isDebug) {
            Log.v(tag, getMsgFormat(msg))
        }
    }

    @JvmStatic
    fun d(msg: String?) {
        if (isDebug) {
            Log.d(TAG, getMsgFormat(msg))
        }
    }

    @JvmStatic
    fun d(tag: String, msg: String?) {
        if (isDebug) {
            Log.d(tag, getMsgFormat(msg))
        }
    }

    @JvmStatic
    fun i(msg: String?) {
        if (isDebug) {
            Log.i(TAG, getMsgFormat(msg))
        }
    }

    @JvmStatic
    fun i(tag: String, msg: String?) {
        if (isDebug) {
            Log.i(tag, getMsgFormat(msg))
        }
    }

    @JvmStatic
    fun w(msg: String?) {
        if (isDebug) {
            Log.w(TAG, getMsgFormat(msg))
        }
    }

    @JvmStatic
    fun w(tag: String, msg: String?) {
        if (isDebug) {
            Log.w(tag, getMsgFormat(msg))
        }
    }

    @JvmStatic
    fun e(msg: String?) {
        if (isDebug) {
            Log.e(TAG, getMsgFormat(msg))
        }
    }

    @JvmStatic
    fun e(tag: String, msg: String?) {
        if (isDebug) {
            Log.e(tag, getMsgFormat(msg))
        }
    }

    @JvmStatic
    fun printJson(tag: String, msg: String?) {
        if (isDebug) {
            printJson(tag, msg, null)
        }
    }

    @JvmStatic
    @Synchronized fun printJson(tag: String, msg: String?, headString: String?) {
        if (isDebug) {
            var message: String = ""
            if (msg != null) {
                message = try {
                    when {
                        msg.startsWith("{") -> {
                            val jsonObject = JSONObject(msg)
                            jsonObject.toString(4)//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
                        }
                        msg.startsWith("[") -> {
                            val jsonArray = JSONArray(msg)
                            jsonArray.toString(4)
                        }
                        else -> msg
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    msg
                }
            }

            printLine(tag, true)
            val headStr = if (headString.isNullOrBlank()) "" else headString + LINE_SEPARATOR
            message = headStr + message
            val lines = LINE_SEPARATOR?.let { message.split(it) }
            lines?.let {
                for (line in it) {
                    Log.d(tag, "║ $line")
                }
            }
            printLine(tag, false)
        }
    }

    /**
     * 获取相关数据:类名,方法名,行号等.用来定位行<br>
     * at cn.utils.MainActivity.onCreate(MainActivity.java:17) 就是用來定位行的代碼<br>
     *
     * @return [ Thread:main, at
     * cn.utils.MainActivity.onCreate(MainActivity.java:17)]
     */
    private fun getFunctionName(): String {
        val stackTraceElements = Thread.currentThread().stackTrace
        stackTraceElements.let {
            for (element in it) {
                if (element.isNativeMethod) {
                    continue
                }
                if (element.className == Thread::class.java.name) {
                    continue
                }
                if (element.className == LogUtils::class.java.name) {
                    continue
                }
                return "[ Thread:" + Thread.currentThread().name + ", at " + element.className + "." +
                        element.methodName + "(" + element.fileName + ":" + element.lineNumber + ")" + " ]"
            }
        }
        return ""
    }

    /**
     * 输出格式定义
     */
    private fun getMsgFormat(msg: String?): String {
        return msg + " ;" + getFunctionName()
    }

    private fun printLine(tag: String, isTop: Boolean) {
        if (isTop) {
            Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
        }
    }

}