package com.v.base.utils


import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.v.base.R


/**
 * 加载图片
 * @param any 图片资源Glide所支持的
 * @param roundingRadius 图片圆角角度
 * @param errorResId 加载错误占位图
 */
fun ImageView.load(
    any: Any,
    roundingRadius: Float = 0f,
    errorResId: Int = R.mipmap.base_iv_default
) = loadDispose(this, any, roundingRadius, errorResId)


/**
 * 加载圆形图片
 * @param any 图片资源Glide所支持的
 * @param errorResId 加载错误占位图
 */
fun ImageView.loadCircle(any: Any, errorResId: Int = R.mipmap.base_iv_default) =
    loadDispose(this, any, -1f, errorResId)


/**
 * 加载图片监听
 * @param any 图片资源Glide所支持的
 * @param w 图片设置宽度
 * @param h 图片设置高度
 * @param success 图片加载成功
 * @param error 图片加载失败
 */
fun Context.loadListener(
    any: Any,
    w: Int,
    h: Int,
    success: ((Drawable) -> Unit),
    error: (() -> Unit)? = null
) = run {

    Glide.with(this)
        .asDrawable()
        .override(w, h)
        .load(any)
        .into(object : CustomTarget<Drawable>() {

            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable?>?
            ) {
                success(resource)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                error?.run {
                    this@run
                }
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

}

/**
 * 图片的处理
 */
private fun loadDispose(
    image: ImageView,
    any: Any,
    roundingRadius: Float,
    errorResId: Int
) {
    image.run {
        try {
            var options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errorResId)
                .placeholder(errorResId)
                .dontAnimate()

            when {
                roundingRadius > 0 -> {
                    this.scaleType = ImageView.ScaleType.CENTER_CROP
                    var radius = roundingRadius.toInt().dp2px()
                    options.transform(CenterCrop(), RoundedCorners(radius)) //圆角

                    Glide.with(this.context)
                        .load(any)
                        .apply(options)
                        .thumbnail(loadRoundedTransform(this.context, errorResId, radius))
                        .thumbnail(loadRoundedTransform(this.context, errorResId, radius))
                        .into(this)
                }
                roundingRadius == -1f -> {
                    this.scaleType = ImageView.ScaleType.CENTER_CROP
                    options.transform(CenterCrop(), CircleCrop()) //圆形

                    Glide.with(this.context)
                        .load(any)
                        .apply(options)
                        .thumbnail(loadCircleTransform(this.context, errorResId))
                        .thumbnail(loadCircleTransform(this.context, errorResId))
                        .into(this)
                }
                else -> {
                    Glide.with(this.context)
                        .load(any)
                        .apply(options)
                        .into(this)
                }
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


/**
 * 对占位图的圆角处理
 */
private fun loadRoundedTransform(
    context: Context,
    @DrawableRes placeholderId: Int,
    radius: Int
): RequestBuilder<Drawable?>? {
    return Glide.with(context)
        .load(placeholderId)
        .apply(
            RequestOptions()
                .transform(CenterCrop(), RoundedCorners(radius))
        )
}


/**
 * 对占位图的圆形处理
 */
fun loadCircleTransform(
    context: Context,
    @DrawableRes placeholderId: Int
): RequestBuilder<Drawable?>? {
    return Glide.with(context)
        .load(placeholderId)
        .apply(
            RequestOptions()
                .transform(CenterCrop(), CircleCrop())
        )
}


