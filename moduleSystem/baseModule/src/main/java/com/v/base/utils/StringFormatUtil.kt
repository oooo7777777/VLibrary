package com.v.base.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.v.base.BaseApplication
import com.v.base.utils.ToastUtil
import java.util.*




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
fun randomNumber(number: Int): Int {
    val random = Random()
    return random.nextInt(number)
}

/**
 * 生成随机数
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
fun String.toast() {
    ToastUtil.showToast(BaseApplication.getContext(), this)
}

/**
 * 复制文本到粘贴板
 */
fun Context.copyToClipboard(text: String) {

    val cm = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val mClipData = ClipData.newPlainText("Label", text)
    cm!!.setPrimaryClip(mClipData)
    "内容已复制到粘贴板".toast()
}
