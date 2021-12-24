package com.v.base.utils

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.v.base.R
import com.v.base.utils.ext.vbCopyToClipboard
import com.v.base.utils.ext.vbInvalidClick
import com.v.base.utils.ext.vbOnClickAnimator


/**
 * 设置ImageView图片
 */
@BindingAdapter(
    value = ["vb_img_url", "vb_img_radius", "vb_img_circle", "vb_img_error_res_id"],
    requireAll = false
)
fun ImageView.vbImgUrl(
    any: Any?,
    roundingRadius: Float = 0f,
    circle: Boolean = false,
    errorResId: Int = R.mipmap.vb_iv_empty
) {

    any?.let {
        if (circle) {
            this.vbLoadCircle(it, errorResId)
        } else {
            this.vbLoad(it, roundingRadius, errorResId)
        }
    }


}


/**
 * 设置TextView DrawableLeft
 */
@BindingAdapter(
    value = ["vb_drawable_Left", "vb_drawable_right", "vb_drawable_width", "vb_drawable_height"],
    requireAll = false
)
fun TextView.vbDrawable(anyLeft: Any?, anyRight: Any?, w: Int, h: Int) {

    if (anyLeft != null) {
        this.context.vbLoadListener(anyLeft, w.vbDp2px(), h.vbDp2px(),
            success = {
                it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
                this@vbDrawable.setCompoundDrawables(it, null, null, null)
            })
    }
    if (anyRight != null) {
        this.context.vbLoadListener(anyRight, w.vbDp2px(), h.vbDp2px(),
            success = {
                it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)
                this@vbDrawable.setCompoundDrawables(null, null, it, null)
            })
    }
}


/**
 * 打电话
 */
@BindingAdapter(value = ["vb_call_phone"], requireAll = false)
fun View.vbCallPhone(phone: Any) {
    if (!phone.toString().isNullOrEmpty()) {
        vbOnClickAnimator {
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
@BindingAdapter(value = ["vb_copy_to_clipboard"], requireAll = false)
fun View.vbCopyToClipboard(result: Any) {

    if (!result.toString().isNullOrEmpty()) {
        vbOnClickAnimator {
            this.context.vbCopyToClipboard(result.toString())
        }
    }
}


/**
 * view点击动画以及添加间隔做处理
 */
@BindingAdapter(value = ["vb_click", "vb_click_time"], requireAll = false)
fun View.vbClick(onClickListener: View.OnClickListener?, clickTime: Long) {

    if (onClickListener != null) {
        vbOnClickAnimator(if (clickTime <= 0) 500L else clickTime) {
            onClickListener.onClick(it)
        }
    }

}

/**
 * 字体加粗
 */
@BindingAdapter(value = ["vb_text_bold"], requireAll = false)
fun TextView.vbTextBold(boolean: Boolean) {
    this.paint.isFakeBoldText = boolean
}


/**
 * 关闭当前界面
 * @param isFinish 是否启用
 */
@BindingAdapter(value = ["vb_finish"], requireAll = false)
fun View.vbFinishActivity(isFinish: Boolean) {
    if (isFinish) {
        var temp = context
        var activity: Activity? = null

        while (temp is ContextWrapper) {
            if (temp is Activity) {
                activity = temp
            }
            temp = temp.baseContext
        }

        val finalActivity = activity
        vbOnClickAnimator(500L) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finalActivity!!.finishAfterTransition()
            } else {
                finalActivity!!.finish()
            }
        }

    }
}


/**
 * 如果view是TextView 设置文字同时做判断
 * 如果是其他的View
 * 文字等于NullOrEmpty 就INVISIBLE VIEW
 */
@BindingAdapter(value = ["vb_view_visible"], requireAll = false)
fun View.vbViewVisible(text: String?) {

    if (this is TextView) {
        if (text.isNullOrEmpty()) {
            this.visibility = View.INVISIBLE
        } else {
            this.text = text
            this.visibility = View.VISIBLE
        }

    } else {
        if (text.isNullOrEmpty()) {
            this.visibility = View.INVISIBLE
        } else {
            this.visibility = View.VISIBLE
        }
    }

}

/**
 * 如果view是TextView 设置文字同时做判断
 * 如果是其他的View
 * 如果文字等于NullOrEmpty 就GONE VIEW
 */
@BindingAdapter(value = ["vb_view_gone"], requireAll = false)
fun View.vbViewGone(text: String?) {
    if (this is TextView) {
        if (text.isNullOrEmpty()) {
            this.visibility = View.GONE
        } else {
            this.text = text
            this.visibility = View.VISIBLE
        }

    } else {
        if (text.isNullOrEmpty()) {
            this.visibility = View.GONE
        } else {
            this.visibility = View.VISIBLE
        }
    }
}

/**
 * 设置文字中间横线
 */
@BindingAdapter(value = ["vb_text_line_center"], requireAll = false)
fun TextView.vbTextCenterLine(boolean: Boolean) = run {
    this.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG //中间横线
    this.paint.isAntiAlias = true
}

/**
 * 设置文字下划线
 */
@BindingAdapter(value = ["vb_text_line_bottom"], requireAll = false)
fun TextView.vbTextLineBottom(boolean: Boolean) = run {
    this.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
    this.paint.isAntiAlias = true//抗锯齿
}


/**
 * 设置文字 不管类型全部转换成string
 */
@BindingAdapter(value = ["vb_text_format"], requireAll = false)
fun TextView.vbTextFormat(any: Any?) = run {
    any?.run {
        text = this.toString()
    }
}

