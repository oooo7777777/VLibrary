package com.v.base.utils

import android.app.Application
import android.util.TypedValue
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.v.base.VBApplication
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
 * dp2px
 */
fun Number.vbDp2px(): Int = run {

    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), VBApplication.getApplication().resources.displayMetrics
    ).toInt()

}

/**
 * sp2px
 */
fun Number.vbSp2px(): Int = run {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(), VBApplication.getApplication().resources.displayMetrics
    ).toInt()
}

/**
 * px2dp
 */
fun Int.vbPx2dp(): Int = run {
    val scale: Float = VBApplication.getApplication().resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

/**
 * px2sp
 */
fun Int.vbPx2sp(): Int = run {
    val fontScale: Float = VBApplication.getApplication().resources.displayMetrics.scaledDensity
    return (this / fontScale + 0.5).toInt()
}

/**
 * 随机颜色
 */
val vbGetRandomColor: Int
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
fun Int.vbGetRandomNumber(): Int = run {
    val random = Random()
    return random.nextInt(this)
}

/**
 * 生成随机数
 * @param min 最小数字
 * @param max 最大数字
 */
fun vbGetRandomNumber(min: Int, max: Int): Int {
    val random = Random()
    return random.nextInt(max) % (max - min + 1) + min
}


/**
 * toast
 */
fun Any.toast(isLong: Boolean = false, gravity: Int = Gravity.CENTER) {

    var toast = Toast.makeText(VBApplication.getApplication(),
        this.toString(),
        if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    toast.setGravity(gravity, 0, 0);
    toast.show()
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