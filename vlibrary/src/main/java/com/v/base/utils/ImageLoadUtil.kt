package com.v.base.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.BaseRequestOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.v.base.R
import jp.wasabeef.glide.transformations.RoundedCornersTransformation
import kotlinx.coroutines.*


/**
 * 加载图片
 * @param any 图片资源Glide所支持的
 * @param roundingRadius 图片圆角角度
 * @param errorResId 加载错误占位图
 */
fun ImageView.vbLoad(
    any: Any,
    roundingRadius: Int = 0,
    errorResId: Int = R.mipmap.vb_iv_empty,
) = loadDispose(this, any, roundingRadius, errorResId, null)


/**
 * 加载不同圆角图片
 * @param any 图片资源Glide所支持的
 * @param topLeft 顶部左边圆角
 * @param topRight 顶部右边圆角
 * @param bottomLeft 底部左边圆角
 * @param bottomRight 底部右边圆角
* @param errorResId 加载错误占位图
 */
fun ImageView.vbLoadRounded(
    any: Any,
    topLeft: Int = 0,
    topRight: Int = 0,
    bottomLeft: Int = 0,
    bottomRight: Int = 0,
    errorResId: Int = R.mipmap.vb_iv_empty,
) {

    //顶部左边圆角
    val tfTopLeft =
        RoundedCornersTransformation(topLeft.vbDp2px(),
            0,
            RoundedCornersTransformation.CornerType.TOP_LEFT)

    //顶部右边圆角
    val tfTopRight =
        RoundedCornersTransformation(topRight.vbDp2px(),
            0,
            RoundedCornersTransformation.CornerType.TOP_RIGHT)

    //底部左边圆角
    val tfBottomLeft =
        RoundedCornersTransformation(bottomLeft.vbDp2px(),
            0,
            RoundedCornersTransformation.CornerType.BOTTOM_LEFT)

    //底部右边圆角
    val tfBottomRight =
        RoundedCornersTransformation(bottomRight.vbDp2px(),
            0,
            RoundedCornersTransformation.CornerType.BOTTOM_RIGHT)

    val tf =
        MultiTransformation(
            CenterCrop(), tfTopLeft, tfTopRight, tfBottomLeft, tfBottomRight)

    loadDispose(this, any, 0, errorResId, tf)
}


/**
 * 加载圆形图片
 * @param any 图片资源Glide所支持的
 * @param errorResId 加载错误占位图
 */
fun ImageView.vbLoadCircle(any: Any, errorResId: Int = R.mipmap.vb_iv_empty) =
    loadDispose(this, any, -1, errorResId, null)


/**
 * 加载图片监听
 * @param any 图片资源Glide所支持的
 * @param w 图片设置宽度
 * @param h 图片设置高度
 * @param success 图片加载成功
 * @param error 图片加载失败
 */
fun Context.vbLoadListener(
    any: Any?,
    w: Int = 0,
    h: Int = 0,
    success: ((Drawable) -> Unit),
    error: (() -> Unit)? = null,
) = run {

    if (any == null) {
        error?.invoke()
    } else {
        Glide.with(this)
            .asDrawable().apply {
                if (w > 0 || h > 0) {
                    override(w, h)
                }
            }
            .load(any)
            .into(object : CustomTarget<Drawable>() {

                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?,
                ) {
                    success.invoke(resource)
                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    error?.invoke()
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }


}

/**
 * 图片的处理
 */
private fun loadDispose(
    image: ImageView,
    any: Any,
    roundingRadius: Int,
    errorResId: Int,
    transformation: Transformation<Bitmap>? = null,
) {
    image.run {


        try {
            val options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .format(DecodeFormat.PREFER_RGB_565)
                .error(errorResId)
                .dontAnimate()

            //不同圆角
            if (transformation != null) {
                options.transform(transformation)
            } else {
                //圆角
                if (roundingRadius > 0) {
                    val radius = roundingRadius.vbDp2px()
                    options.transform(CenterCrop(),
                        RoundedCornersTransformation(
                            radius, 0,
                            RoundedCornersTransformation.CornerType.ALL,
                        ))
                } else if (roundingRadius < 0) {
                    //圆形
                    options.transform(CenterCrop(), RoundedCornersTransformation(1280, 0)) //圆形
                }
            }

            Glide.with(this)
                .load(any)
                .apply(options)
                .apply {
                    thumbnail(loadError(this@run.context,
                        errorResId,
                        options))
                }
                .into(this)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


/**
 * 对占位图的处理
 */
private fun loadError(
    context: Context,
    @DrawableRes placeholderId: Int,
    options: BaseRequestOptions<*>,
): RequestBuilder<Drawable?>? {
    return Glide.with(context)
        .load(placeholderId)
        .apply(options)
}










