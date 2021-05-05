package com.jackom.loglib

import android.util.Log
import androidx.annotation.IntDef

/**
 * @author：jackom
 * @date：4/30/21 on 10:41 AM
 * @desc：log优先级
 */
object LogPriority {
    /**
     * Priority constant for the println method; use Log.v.
     */
    const val V = Log.VERBOSE

    /**
     * Priority constant for the println method; use Log.d.
     */
    const val D = Log.DEBUG

    /**
     * Priority constant for the println method; use Log.i.
     */
    const val I = Log.INFO

    /**
     * Priority constant for the println method; use Log.w.
     */
    const val W = Log.WARN

    /**
     * Priority constant for the println method; use Log.e.
     */
    const val E = Log.ERROR

    /**
     * Priority constant for the println method.
     */
    const val A = Log.ASSERT

    @IntDef(V, D, I, W, E, A)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Priority
}