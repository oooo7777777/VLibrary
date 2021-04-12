package com.v.base.utils

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter


/**
 * 设置ImageView图片
 */
@BindingAdapter(value = ["imgUrl", "imgRadius", "circle"], requireAll = false)
fun ImageView.setImgUrl(
    any: Any?,
    roundingRadius: Float = 0f,
    circle: Boolean = false
) {

    any?.let {
        if (circle) {
            this.loadCircle(it)
        } else {
            this.load(it, roundingRadius)
        }
    }


}


/**
 * 设置TextView DrawableLeft
 */
@BindingAdapter(
    value = ["drawableLeft", "drawableRight", "drawableWidth", "drawableHeight"],
    requireAll = false
)
fun TextView.setDrawable(anyLeft: Any?, anyRight: Any?, w: Int, h: Int) {

    if (anyLeft != null) {
        this.context.loadListener(anyLeft, this.dp2px(w), this.dp2px(h),
            success = {
                it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
                this@setDrawable.setCompoundDrawables(it, null, null, null)
            })
    }
    if (anyRight != null) {
        this.context.loadListener(anyRight, this.dp2px(w), this.dp2px(h),
            success = {
                it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
                this@setDrawable.setCompoundDrawables(null, null, it, null)
            })
    }
}


/**
 * 打电话
 */
@BindingAdapter(value = ["callPhone"], requireAll = false)
fun View.callPhone(phone: Any) {
    if (!phone.toString().isNullOrEmpty()) {
        onClickAnimator {
            try {
                val intent = Intent(Intent.ACTION_DIAL)
                val data = Uri.parse("tel:$phone")
                intent.data = data
                this.context.startActivity(intent)
            } catch (e: Exception) {
                "没有获取打电话权限".toast()
            }
        }

    }
}

/**
 * 复制文本到粘贴板
 */
@BindingAdapter(value = ["copyToClipboard"], requireAll = false)
fun View.copyToClipboard(result: Any) {

    if (!result.toString().isNullOrEmpty()) {
        onClickAnimator {
            this.context.copyToClipboard(result.toString())
        }
    }
}


/**
 * view点击动画以及添加间隔做处理
 */
@BindingAdapter(value = ["click", "clickTime"], requireAll = false)
fun View.click(onClickListener: View.OnClickListener?, clickTime: Long) {

    if (onClickListener != null) {
        onClickAnimator(if (clickTime <= 0) 500L else clickTime) {
            onClickListener.onClick(it)
        }
    }

}

/**
 * 字体加粗
 */
@BindingAdapter(value = ["textBold"], requireAll = false)
fun TextView.textBold(boolean: Boolean) {
    this.paint.isFakeBoldText = boolean
}








