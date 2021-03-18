package com.v.base.utils

import android.content.Context
import android.view.View
import com.v.base.BaseApplication


/**
 * dp转px
 */
fun Float.dp2px(): Int =
    run {
        val scale = BaseApplication.getContext().resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

/**
 * dp转px
 */
fun View.dp2px(dp: Int): Int =
    run {
        val scale = BaseApplication.getContext().resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }



/**
 * 获取屏幕的高度（单位：px
 */
fun Context.getScreenHeight(): Int =run{
    resources.displayMetrics.heightPixels
}


/**
 * 获取屏幕的宽度
 */
fun Context.getScreenWidth(): Int = run {
    resources.displayMetrics.widthPixels
}

/**
 * 获取状态栏高度
 */
fun Context.getStatusBarHeight(): Int = run {
    var result = -1
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}