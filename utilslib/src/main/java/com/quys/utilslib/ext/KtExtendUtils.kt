package com.quys.utilslib.ext

import android.os.SystemClock
import android.view.View
import android.widget.TextView


/**
 *  author : zhengminxin
 *  date : 2019/7/17 15:58
 *  desc : 扩展函数工具类
 */

fun TextView.checkBlank(message: String): String? {
    val text = this.text.toString()
    if (text.isBlank()) {
        return null
    }
    return text
}


//link: https://blog.csdn.net/weixin_34166847/article/details/86936244
private var triggerLastTime: Long = 0
private const val TRIGGER_DELAY: Long = 1000  // 两次点击间隔不能少于1000ms

fun <T : View> T.clickEnable(): Boolean {
    return clickEnable(TRIGGER_DELAY)
}

fun <T : View> T.clickEnable(triggerDelay: Long): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

//可点击的最大次数
private const val MAX_CLICK_COUNTS: Int = 5
//可点击的最大时间间隔(ms)
private const val CLICK_MAX_COUNT_TRIGGER_DELAY: Long = 1000
private var curClickCounts: LongArray = LongArray(MAX_CLICK_COUNTS)

/**
 * 原理如下：
自定义一个空数组，每次把数组整体向前移动一位，然后给数组最后一位赋值一个时间数，
当SystemClock.uptimeMillis() 与数组第一位之间的时间差小于CLICK_MAX_COUNT_TRIGGER_DELAY时，生效。
 */
fun <T : View> T.continuousClick(): Boolean {
    return continuousClick(MAX_CLICK_COUNTS, CLICK_MAX_COUNT_TRIGGER_DELAY)
}

fun <T : View> T.continuousClick(maxLimitCount: Int, mxLimitTime: Long): Boolean {
    //每次点击时，数组向前移动一位(把从第二位至最后一位之间的数字复制到第一位至倒数第一位)
    System.arraycopy(curClickCounts, 1, curClickCounts, 0, curClickCounts.size - 1)
    //为数组最后一位赋值(记录一个时间)
    curClickCounts[curClickCounts.size - 1] = SystemClock.uptimeMillis()
    if (curClickCounts[0] >= SystemClock.uptimeMillis() - mxLimitTime) {
        //mxLimitTime时间内连续点击
        //这里说明一下，我们在进来以后需要还原状态，否则如果点击过快，第六次，第七次 都会不断进来触发该效果。重新开始计数即可
        curClickCounts = LongArray(maxLimitCount) //重新初始化数组
        return true
    }
    return false
}

/**
 * 检查数组中的元素是否为null，全不为null时才会触发回调
 */
inline fun <R> checkArgsNotNull(vararg args: Any?, block: () -> R) {
    when (args.filterNotNull().size) {
        args.size -> block()
    }
}

inline fun <reified T : Enum<T>> printAllValues() {
    print(enumValues<T>().joinToString { it.name })
}