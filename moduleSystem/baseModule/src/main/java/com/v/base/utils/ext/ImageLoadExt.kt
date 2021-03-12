package com.v.base.utils.ext


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


fun ImageView.load(
    any: Any,
    roundingRadius: Float = 0f,
    errorResId: Int = R.mipmap.base_iv_default
) = loadDispose(this, any, roundingRadius, errorResId)


fun ImageView.loadAvatar(any: Any,roundingRadius: Float = 0f, errorResId: Int = R.mipmap.base_iv_default) =
    loadDispose(this, any, 0f, errorResId)


fun ImageView.loadCircle(any: Any, errorResId: Int = R.mipmap.base_iv_default) =
    loadDispose(this, any, -1f, errorResId)


fun loadDispose(
    image: ImageView,
    any: Any,
    roundingRadius: Float = 0f,
    errorResId: Int = R.mipmap.base_iv_default
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
                    var radius = roundingRadius.dp2px()
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


//对占位图的圆角处理
fun loadRoundedTransform(
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

//对占位图的圆形处理
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


fun Context.loadListener(
    url: Any,
    w: Int,
    h: Int,
    success: ((Drawable) -> Unit),
    error: (() -> Unit)
) = run {

    Glide.with(this)
        .asDrawable()
        .override(w, h)
        .load(url)
        .into(object : CustomTarget<Drawable>() {

            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable?>?
            ) {
                success(resource)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                error()
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })

}