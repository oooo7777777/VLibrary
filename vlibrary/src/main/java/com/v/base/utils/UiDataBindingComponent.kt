package com.v.base.utils

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.v.base.R


/**
 * 设置ImageView图片
 */
@BindingAdapter(
    value = ["vb_img_url", "vb_img_radius", "vb_img_circle", "vb_img_error_res_id", "vb_img_top_left", "vb_img_top_right", "vb_img_bottom_left", "vb_img_bottom_right", "vb_img_stroke_width", "vb_img_stroke_color", "vb_img_color"],
    requireAll = false
)
fun ImageView.vbImgUrl(
    any: Any?,
    cornersRadius: Int = 0,
    isCircle: Boolean = false,
    errorResId: Int = R.mipmap.vb_iv_empty,
    topLeft: Int = 0,
    topRight: Int = 0,
    bottomLeft: Int = 0,
    bottomRight: Int = 0,
    strokeWidth: Int = 0,
    @ColorInt strokeColor: Int = Color.TRANSPARENT,
    @ColorInt imgColor: Int = Color.TRANSPARENT,
) {

    any?.let {
        //圆形
        this.vbLoad(
            any = any,
            cornersRadius = cornersRadius,
            topLeft = topLeft,
            topRight = topRight,
            bottomLeft = bottomLeft,
            bottomRight = bottomRight,
            errorResId = errorResId,
            strokeWidth = strokeWidth,
            strokeColor = strokeColor,
            isCircle = isCircle,
            imgColor = imgColor
        )
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
    bottomH: Int? = null,

    ) {


    this.context.vbLoadListener<Drawable>(left,
        (leftW ?: w).vbDp2px2Int(),
        (leftH ?: h).vbDp2px2Int(),
        success = {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)

            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]

            this@vbDrawable.setCompoundDrawables(it, drawableTop, drawableRight, drawableBottom)
        },
        error = {
            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]

            this@vbDrawable.setCompoundDrawables(
                null,
                drawableTop,
                drawableRight,
                drawableBottom
            )
        })




    this.context.vbLoadListener<Drawable>(right,
        (rightW ?: w).vbDp2px2Int(),
        (rightH ?: h).vbDp2px2Int(),
        success = {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)

            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]


            this@vbDrawable.setCompoundDrawables(drawableLeft, drawableTop, it, drawableBottom)
        },
        error = {
            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]


            this@vbDrawable.setCompoundDrawables(
                drawableLeft,
                drawableTop,
                null,
                drawableBottom
            )
        })




    this.context.vbLoadListener<Drawable>(top, (topW ?: w).vbDp2px2Int(), (topH ?: h).vbDp2px2Int(),
        success = {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)

            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]


            this@vbDrawable.setCompoundDrawables(
                drawableLeft,
                it,
                drawableRight,
                drawableBottom
            )
        }, error = {
            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]


            this@vbDrawable.setCompoundDrawables(
                drawableLeft,
                null,
                drawableRight,
                drawableBottom
            )
        })


    this.context.vbLoadListener<Drawable>(bottom,
        (bottomW ?: w).vbDp2px2Int(),
        (bottomH ?: h).vbDp2px2Int(),
        success = {
            it.setBounds(0, 0, it.intrinsicWidth, it.intrinsicHeight)

            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]

            this@vbDrawable.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, it)
        },
        error =
        {

            val drawableLeft: Drawable? = compoundDrawables[0]
            val drawableTop: Drawable? = compoundDrawables[1]
            val drawableRight: Drawable? = compoundDrawables[2]
            val drawableBottom: Drawable? = compoundDrawables[3]

            this@vbDrawable.setCompoundDrawables(drawableLeft, drawableTop, drawableRight, null)
        })


}


/**
 * view点击(动画根据全局的设定的参数来配置)
 */
@BindingAdapter(
    value = ["vb_click"],
    requireAll = false
)
fun View.vbClick(
    onClickListener: View.OnClickListener?,
) {
    if (onClickListener != null) {
        this.vbOnClickListener {
            onClickListener.onClick(it)
        }
    }
}

/**
 * view点击(带动画)
 */
@BindingAdapter(
    value = ["vb_click_animator_on"],
    requireAll = false
)
fun View.vbClickAnimatorOn(
    onClickListener: View.OnClickListener?,
) {
    if (onClickListener != null) {
        this.vbOnClickListener(true) {
            onClickListener.onClick(it)
        }
    }
}

/**
 * view点击(不带动画)
 */
@BindingAdapter(
    value = ["vb_click_animator_off"],
    requireAll = false
)
fun View.vbClickAnimatorOff(
    onClickListener: View.OnClickListener?,
) {
    if (onClickListener != null) {
        this.vbOnClickListener(false) {
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

        this.vbOnClickListener {
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
 * @param boolean 是否需要设置下划线
 */
@BindingAdapter(value = ["vb_text_line_bottom"], requireAll = false)
fun TextView.vbTextLineBottom(boolean: Boolean) = run {
    this.paint.flags = Paint.UNDERLINE_TEXT_FLAG //下划线
    this.paint.isAntiAlias = true//抗锯齿
}


/**
 * 设置文字 不管类型全部转换成string
 * @param any 需要转换的文字 vb_text_format
 */
@BindingAdapter(value = ["vb_text_format"], requireAll = false)
fun TextView.vbTextFormat(any: Any?) = run {
    any?.run {
        text = this.toString()
    }
}


/**
 * 设置控件阴影
 * @param color 阴影颜色 vb_shadow_color
 * @param ev 阴影大小 vb_shadow_color_elevation
 */
@BindingAdapter(value = ["vb_shadow_color", "vb_shadow_color_elevation"], requireAll = false)
fun View.vbShadowColor(color: String?, ev: Float = 10f) = run {
    color?.run {
        elevation = ev
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            outlineAmbientShadowColor = Color.parseColor(color)// 环境阴影
            outlineSpotShadowColor = Color.parseColor(color)// 点阴影
        }
    }
}