package com.v.base.utils

import android.app.Application
import android.util.TypedValue
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.v.base.BaseApplication
import java.util.*


private val viewModelMap: MutableMap<Class<*>, ViewModel?> =
    HashMap()

/**
 * 获取全局唯一的ViewModel
 * 常用与跨页面修改数据（并且需要刷新显示），比如在某个页面对该ViewModel里的MutableLiveData进行了observe，
 * 无论在哪里修改ViewModel里面的MutableLiveData的值，这个页面都会收到通知（页面在活跃状态下马上收到，非活跃状态下将在变为活跃状态的那一刻收到），收到通知后调用onChanged()方法（一般是刷新视图）
 *
 * @param application    本项目所设置使用的application实体
 * @param viewModelClass ViewModel对应的类
 * @param <T>
 * @return
 */
fun <T : ViewModel?> getApplicationViewModel(
    application: Application?,
    viewModelClass: Class<T>
): T {
    if (viewModelMap.containsKey(viewModelClass)) {
        return viewModelMap[viewModelClass] as T
    }
    val t =
        ViewModelProvider.AndroidViewModelFactory.getInstance(application!!).create(viewModelClass)
    viewModelMap[viewModelClass] = t
    return t
}


/**
 * dp转px
 */

//fun Int.dp2px(): Int =
//    run {
//        val scale = BaseApplication.getContext().resources.displayMetrics.density
//        return (this * scale + 0.5f).toInt()
//    }


/**
 * dp2px
 */
fun Int.dp2px(): Int = run {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), BaseApplication.getContext().resources.displayMetrics
    ).toInt()
}

/**
 * sp2px
 */
fun Int.sp2px(): Int = run {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(), BaseApplication.getContext().resources.displayMetrics
    ).toInt()
}

/**
 * px2dp
 */
fun Int.px2dp(): Int = run {
    val scale: Float = BaseApplication.getContext().resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

/**
 * px2sp
 */
fun Int.px2sp(): Int = run {
    val fontScale: Float = BaseApplication.getContext().resources.displayMetrics.scaledDensity
    return (this / fontScale + 0.5).toInt()
}

/**
 * 随机颜色
 */
val randomColor: Int
    get() {
        var c = 0xffffff
        val random = Random()
        val color = -0x1000000 or random.nextInt(0xffffff)
        c = color
        return c
    }


/**
 * 生成随机数
 */
fun Int.randomNumber(): Int = run {
    val random = Random()
    return random.nextInt(this)
}

/**
 * 生成随机数
 * @param min 最小数字
 * @param max 最大数字
 */
fun randomNumber(min: Int, max: Int): Int {
    val random = Random()
    return random.nextInt(max) % (max - min + 1) + min
}

/**
 * 手机星号
 */
fun String.phoneNumberFormat(): String = run {
    return this.replace(("(\\d{3})\\d{4}(\\d{4})").toRegex(), "$1****$2")
}

/**
 * toast
 */
fun Any.toast() {
    Toast.makeText(BaseApplication.getContext(), this.toString(), Toast.LENGTH_SHORT).show()
}

/**
 * 颜色转换成灰度值
 * @param rgb 颜色
 * @return　灰度值
 */
fun toGrey(rgb: Int): Int {
    val blue = rgb and 0x000000FF
    val green = rgb and 0x0000FF00 shr 8
    val red = rgb and 0x00FF0000 shr 16
    return red * 38 + green * 75 + blue * 15 shr 7
}

fun isWhiteColor(color: Int): Boolean {
    val grey = toGrey(color)
    return grey > 200
}