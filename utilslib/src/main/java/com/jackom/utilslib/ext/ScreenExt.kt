package com.jackom.utilslib.ext

import android.content.Context
import android.view.View

/**
 * Created by luyao
 * on 2019/6/14 14:23
 */

fun <T> Any?.notNull(f: () -> T, t: () -> T): T {
    return if (this != null) f() else t()
}

fun Context.dp2px(dp: Float): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Context.px2dp(px: Float): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun View.dp2px(dp: Float): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun View.px2dp(px: Float): Int {
    val scale = resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

fun Any.dp2px(context: Context, dp: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

fun Any.px2dp(context: Context, px: Float): Int {
    val scale = context.resources.displayMetrics.density
    return (px / scale + 0.5f).toInt()
}

/**
 * 将px值转换为sp值，保证文字大小不变
 *
 * @param pxValue
 * @param fontScale
 * （DisplayMetrics类中属性scaledDensity）
 * @return
 */
fun Context.px2sp(pxValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

/**
 * 将sp值转换为px值，保证文字大小不变
 *
 * @param spValue
 * @param fontScale
 * （DisplayMetrics类中属性scaledDensity）
 * @return
 */
fun Context.sp2px(spValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

/**
 * 将px值转换为sp值，保证文字大小不变
 *
 * @param pxValue
 * @param fontScale
 * （DisplayMetrics类中属性scaledDensity）
 * @return
 */
fun View.px2sp(pxValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

/**
 * 将sp值转换为px值，保证文字大小不变
 *
 * @param spValue
 * @param fontScale
 * （DisplayMetrics类中属性scaledDensity）
 * @return
 */
fun View.sp2px(spValue: Float): Int {
    val fontScale = resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

/**
 * 将px值转换为sp值，保证文字大小不变
 *
 * @param pxValue
 * @param fontScale
 * （DisplayMetrics类中属性scaledDensity）
 * @return
 */
fun Any.px2sp(context: Context, pxValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (pxValue / fontScale + 0.5f).toInt()
}

/**
 * 将sp值转换为px值，保证文字大小不变
 *
 * @param spValue
 * @param fontScale
 * （DisplayMetrics类中属性scaledDensity）
 * @return
 */
fun Any.sp2px(context: Context, spValue: Float): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (spValue * fontScale + 0.5f).toInt()
}

fun View.screenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

fun View.screenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

val Context.screenWidth
    get() = resources.displayMetrics.widthPixels

val Context.screenHeight
    get() = resources.displayMetrics.heightPixels