package com.v.base.utils

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.v.base.R
import com.v.base.utils.ext.log
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
    value = ["vb_drawable_left", "vb_drawable_right",
        "vb_drawable_top", "vb_drawable_bottom",
        "vb_drawable_width", "vb_drawable_height",
        "vb_drawable_left_width", "vb_drawable_left_height",
        "vb_drawable_right_width", "vb_drawable_right_height",
        "vb_drawable_top_width", "vb_drawable_top_height",
        "vb_drawable_bottom_width", "vb_drawable_bottom_height"],
    requireAll = false
)
fun TextView.vbDrawable(
    left: Any? = compoundDrawables[0],
    right: Any? = compoundDrawables[2],
    top: Any? = compoundDrawables[1],
    bottom: Any? = compoundDrawables[3],
    w: Int = 0,
    h: Int = 0,
    leftW: Int? = null,
    leftH: Int? = null,
    rightW: Int? = null,
    rightH: Int? = null,
    topW: Int? = null,
    topH: Int? = null,
    bottomW: Int? = null,
    bottomH: Int? = null

) {


    this.context.vbLoadListener(left, (leftW ?: w).vbDp2px(), (leftH ?: h).vbDp2px(),
        success = {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)

            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]

            this@vbDrawable.setCompoundDrawables(it, drawableTop, drawableRight, drawableBottom)
        }, error = {
            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]

            this@vbDrawable.setCompoundDrawables(null, drawableTop, drawableRight, drawableBottom)
        })




    this.context.vbLoadListener(right, (rightW ?: w).vbDp2px(), (rightH ?: h).vbDp2px(),
        success = {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)

            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]


            this@vbDrawable.setCompoundDrawables(drawableLeft, drawableTop, it, drawableBottom)
        }, error = {
            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]


            this@vbDrawable.setCompoundDrawables(drawableLeft, drawableTop, null, drawableBottom)
        })




    this.context.vbLoadListener(top, (topW ?: w).vbDp2px(), (topH ?: h).vbDp2px(),
        success = {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)

            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]


            this@vbDrawable.setCompoundDrawables(drawableLeft,
                it,
                drawableRight,
                drawableBottom)
        }, error = {
            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]


            this@vbDrawable.setCompoundDrawables(drawableLeft,
                null,
                drawableRight,
                drawableBottom)
        })


    this.context.vbLoadListener(bottom, (bottomW ?: w).vbDp2px(), (bottomH ?: h).vbDp2px(),
        success = {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)

            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]

            this@vbDrawable.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, it)
        }, error =
        {

            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]

            this@vbDrawable.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, null)
        })


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
@BindingAdapter(value = ["vb_click", "vb_click_time", "vb_click_animation_cancel"],
    requireAll = false)
fun View.vbClick(
    onClickListener: View.OnClickListener?,
    clickTime: Long,
    animationCancel: Boolean = false
) {
    if (onClickListener != null) {
        if (animationCancel) {
            if (!this.vbInvalidClick(clickTime)) {
                this.setOnClickListener(onClickListener)
            }
        } else {
            vbOnClickAnimator(if (clickTime <= 0) 500L else clickTime) {
                onClickListener.onClick(it)
            }
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
 * 入参类型为:String 空为隐藏 非空为显示
 * 入参类型为:Boolean true为显示 false为隐藏
 * 入参类型为:Int 1为显示 其他为隐藏
 */
@BindingAdapter(value = ["vb_view_visible"], requireAll = false)
fun View.vbViewVisible(any: Any?) {

    var visibility = View.VISIBLE
    when (any) {
        is String -> {
            visibility = if (any.isNullOrEmpty()) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }
        }
        is Boolean -> {
            visibility = if (any) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
        is Int -> {
            visibility = if (any == 1) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
        }
    }

    this.visibility = visibility
}

/**
 * 入参类型为:String 空为隐藏 非空为显示
 * 入参类型为:Boolean true为显示 false为隐藏
 * 入参类型为:Int 1为显示 其他为隐藏
 */
@BindingAdapter(value = ["vb_view_gone"], requireAll = false)
fun View.vbViewGone(any: Any?) {

    var visibility = View.GONE
    when (any) {
        is String -> {
            visibility = if (any.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }
        is Boolean -> {
            visibility = if (any) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        is Int -> {
            visibility = if (any == 1) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    this.visibility = visibility
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


/**
 * 设置图片颜色
 */
@BindingAdapter(value = ["vb_img_color"], requireAll = false)
fun ImageView.vbSetColor(color: String?) = run {
    color?.run {
        setColorFilter(Color.parseColor(color))
    }

}







