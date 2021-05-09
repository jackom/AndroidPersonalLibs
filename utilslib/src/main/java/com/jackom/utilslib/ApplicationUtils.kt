package com.jackom.utilslib

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException

/**
 * @author : zhengminxin
 * @date : 1/7/2021 4:14 PM
 * @desc :
 */
object ApplicationUtils {

    private var mApplication: Application? = null

    @SuppressLint("PrivateApi")
    fun getApplicationByReflect(): Application? {
        if (null != mApplication) {
            return mApplication
        }
        try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val thread: Any? = getActivityThread()
            val app = activityThreadClass.getMethod("getApplication").invoke(thread) ?: return null
            mApplication = app as Application
            return mApplication
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
        return null
    }

    private fun getActivityThread(): Any? {
        val activityThread: Any? = getActivityThreadInActivityThreadStaticField()
        return activityThread ?: getActivityThreadInActivityThreadStaticMethod()
    }

    @SuppressLint("PrivateApi")
    private fun getActivityThreadInActivityThreadStaticField(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val sCurrentActivityThreadField: Field =
                activityThreadClass.getDeclaredField("sCurrentActivityThread")
            sCurrentActivityThreadField.isAccessible = true
            sCurrentActivityThreadField.get(null)
        } catch (e: Exception) {
            Log.e(
                "UtilsActivityLifecycle",
                "getActivityThreadInActivityThreadStaticField: " + e.message
            )
            null
        }
    }

    @SuppressLint("PrivateApi")
    private fun getActivityThreadInActivityThreadStaticMethod(): Any? {
        return try {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            activityThreadClass.getMethod("currentActivityThread").invoke(null)
        } catch (e: Exception) {
            Log.e(
                "UtilsActivityLifecycle",
                "getActivityThreadInActivityThreadStaticMethod: " + e.message
            )
            null
        }
    }

}