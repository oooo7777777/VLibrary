package com.v.base.utils.ext

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import com.v.base.R
import com.v.base.utils.ViewClickAnimatorUtil
import com.v.base.utils.vbDp2px


/**
 * 重设 view 的宽高
 */
fun View.vbSetViewLayoutParams(
    w: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    h: Int = ViewGroup.LayoutParams.WRAP_CONTENT
) = run {
    var lp = this.layoutParams
    lp.width = w
    lp.height = h
    this.layoutParams = lp

}


/**
 * 重设 view 的margins
 */
fun View.vbSetViewMargins(
    left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0
) = run {

    if (this.layoutParams is MarginLayoutParams) {
        val p = this.layoutParams as MarginLayoutParams
        p.setMargins(left.vbDp2px(), top.vbDp2px(), right.vbDp2px(), bottom.vbDp2px())
        this.requestLayout()
    }
}

/**
 * 点击动画效果
 */
fun View.vbOnClickAnimator(clickTime: Long = 500L, onClick: ((v: View) -> Unit)) = run {
    ViewClickAnimatorUtil(this, clickTime, onClick)
    this
}


/**
 * 点击防抖动
 * @param defaultTime 间隔时间
 */
fun View.vbInvalidClick(defaultTime: Long = 500L): Boolean = run {
    val curTimeStamp = System.currentTimeMillis()
    var lastClickTimeStamp: Long = 0
    val o = this.getTag(R.id.vb_invalid_click)
    if (o == null) {
        this.setTag(R.id.vb_invalid_click, curTimeStamp)
        return false
    }
    lastClickTimeStamp = o as Long
    val isInvalid = curTimeStamp - lastClickTimeStamp < defaultTime
    if (!isInvalid) {
        this.setTag(R.id.vb_invalid_click, curTimeStamp)
    }
    return isInvalid
}