package com.v.base.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.v.base.VBConfig
import jp.wasabeef.glide.transformations.ColorFilterTransformation
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


/**
 * 加载图片
 * @param any 图片资源Glide所支持的
 * @param cornersRadius 圆角
 * @param topLeft 顶部左边圆角
 * @param topRight 顶部右边圆角
 * @param bottomLeft 底部左边圆角
 * @param bottomRight 底部右边圆角
 * @param errorResId 加载错误占位图
 * @param strokeWidth 边框宽度
 * @param strokeColor 边框颜色
 * @param isCircle 是否圆形
 */
fun ImageView.vbLoad(
    any: Any,
    cornersRadius: Int = -1,
    topLeft: Int = 0,
    topRight: Int = 0,
    bottomLeft: Int = 0,
    bottomRight: Int = 0,
    errorResId: Int = VBConfig.options.errorResId,
    strokeWidth: Int = 0,
    @ColorInt strokeColor: Int = Color.TRANSPARENT,
    isCircle: Boolean = false,
    @ColorInt imgColor: Int = Color.TRANSPARENT
) {
    var transformation: Transformation<Bitmap>? = null
    if (cornersRadius != -1 || isCircle) {
        val size = if (isCircle) 1280f else cornersRadius.vbDp2px2Float()
        transformation = RoundedCornersBorderTransformation(
            size,
            size,
            size,
            size,
            strokeColor,
            strokeWidth.vbDp2px2Float()
        )
    } else {
        transformation = RoundedCornersBorderTransformation(
            topLeft.vbDp2px2Float(),
            topRight.vbDp2px2Float(),
            bottomLeft.vbDp2px2Float(),
            bottomRight.vbDp2px2Float(),
            strokeColor,
            strokeWidth.vbDp2px2Float()
        )
    }

    val options = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transform(CenterCrop(), transformation, ColorFilterTransformation(imgColor))

    Glide.with(this.context)
        .load(any)
        .apply(options)
        .apply {
            if (errorResId != 0) {
                error(
                    loadError(
                        this@vbLoad.context,
                        errorResId,
                        options
                    )
                )
            }
        }
        .into(this)
}


/**
 * 加载图片监听返回泛型资源（支持 Drawable 和 Bitmap）
 * @param T 图片类型（Drawable 或 Bitmap）
 * @param any 图片资源（Glide 所支持的）
 * @param w 图片设置宽度
 * @param h 图片设置高度
 * @param success 图片加载成功
 * @param error 图片加载失败
 */
inline fun <reified T> Context.vbLoadListener(
    any: Any?,
    w: Int = 0,
    h: Int = 0,
    crossinline success: (T) -> Unit,
    noinline error: (() -> Unit)? = null
) {
    if (any == null) {
        error?.invoke()
        return
    }

    val requestBuilder = when (T::class) {
        Drawable::class -> Glide.with(this).asDrawable() as RequestBuilder<T>
        Bitmap::class -> Glide.with(this).asBitmap() as RequestBuilder<T>
        else -> throw IllegalArgumentException("Unsupported resource type: ${T::class.java}")
    }

    requestBuilder
        .load(any)
        .apply {
            if (w > 0 || h > 0) {
                override(w, h)
            }
        }
        .into(object : CustomTarget<T>() {

            override fun onResourceReady(resource: T & Any, transition: Transition<in T>?) {
                success(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                error?.invoke()
            }
        })
}


/**
 * 对加载错误的占位图的处理成圆角
 */
private fun loadError(
    context: Context,
    placeholderId: Int,
    options: BaseRequestOptions<*>,
): RequestBuilder<Drawable?> {
    return Glide.with(context)
        .load(placeholderId)
        .apply(options)
}