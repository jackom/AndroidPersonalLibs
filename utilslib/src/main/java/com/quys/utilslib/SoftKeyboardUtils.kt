package com.quys.utilslib

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText


/**
 * author : zhengminxin
 * date : 2019/9/5 14:04
 * desc : 软键盘工具类
 */
class SoftKeyboardUtils {

    /**
     * 显示键盘
     * @param et 输入焦点
     */
    fun showInputKeyboard(activity: Activity, et: EditText) {
        et.requestFocus()
        val imm: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * 隐藏键盘
     */
    fun hideInputKeyboard(activity: Activity) {
        val imm: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = activity.window.peekDecorView()
        view?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

}