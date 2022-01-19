package com.v.base.utils


import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.view.View
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
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.v.base.R
import com.v.base.utils.ext.log
import com.v.base.utils.ext.logE
import kotlinx.coroutines.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


/**
 * 加载图片
 * @param any 图片资源Glide所支持的
 * @param roundingRadius 图片圆角角度
 * @param errorResId 加载错误占位图
 */
fun ImageView.vbLoad(
    any: Any,
    roundingRadius: Float = 0f,
    errorResId: Int = R.mipmap.vb_iv_empty
) = loadDispose(this, any, roundingRadius, errorResId)


/**
 * 加载圆形图片
 * @param any 图片资源Glide所支持的
 * @param errorResId 加载错误占位图
 */
fun ImageView.vbLoadCircle(any: Any, errorResId: Int = R.mipmap.vb_iv_empty) =
    loadDispose(this, any, -1f, errorResId)


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
    error: (() -> Unit)? = null
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
                    transition: Transition<in Drawable?>?
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
    roundingRadius: Float,
    errorResId: Int
) {
    image.run {

        var isGif = false
        try {
            isGif = any.toString().endsWith(".gif")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        try {
            var options = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(errorResId)
                .placeholder(errorResId)
                .dontAnimate()

            when {
                roundingRadius > 0 -> {
                    this.scaleType = ImageView.ScaleType.CENTER_CROP
                    var radius = roundingRadius.toInt().vbDp2px()
                    options.transform(CenterCrop(), RoundedCorners(radius)) //圆角

                    if (isGif) {
                        Glide.with(this.context)
                            .asGif()
                            .load(any)
                            .into(this)

                    } else {
                        Glide.with(this.context)
                            .load(any)
                            .apply(options)
                            .thumbnail(loadRoundedTransform(this.context, errorResId, radius))
                            .thumbnail(loadRoundedTransform(this.context, errorResId, radius))
                            .into(this)
                    }
                }
                roundingRadius == -1f -> {
                    this.scaleType = ImageView.ScaleType.CENTER_CROP
                    options.transform(CenterCrop(), CircleCrop()) //圆形

                    if (isGif) {
                        Glide.with(this.context)
                            .asGif()
                            .load(any)
                            .into(this)

                    } else {
                        Glide.with(this.context)
                            .load(any)
                            .apply(options)
                            .thumbnail(loadCircleTransform(this.context, errorResId))
                            .thumbnail(loadCircleTransform(this.context, errorResId))
                            .into(this)
                    }


                }
                else -> {
                    if (isGif) {
                        Glide.with(this.context)
                            .asGif()
                            .load(any)
                            .into(this)

                    } else {
                        Glide.with(this.context)
                            .load(any)
                            .apply(options)
                            .into(this)
                    }
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
private fun loadCircleTransform(
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


/**
 * 保存图片到本地 view,bitmap,url
 */
fun Any.vbSaveLocality(context: Context) = run {
    Thread {
        Looper.prepare()
        when (this) {
            is View -> {
                saveImageToGallery(context, this.vbToBitmap())
            }
            is Bitmap -> {
                saveImageToGallery(context, this)
            }
            is String -> {
                saveImageToGallery(
                    context,
                    Glide.with(context)
                        .asBitmap()
                        .load(this)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get()
                )
            }
            else -> {
                throw IllegalStateException(context.getString(R.string.vb_string_save_locality))
            }
        }
        Looper.loop()
    }.start()
}


/**
 * view转换成bitmap
 */
fun View.vbToBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    if (Build.VERSION.SDK_INT >= 11) {
        this.measure(
            View.MeasureSpec.makeMeasureSpec(this.width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(this.height, View.MeasureSpec.EXACTLY)
        )
        this.layout(
            this.x.toInt(),
            this.y.toInt(),
            this.x.toInt() + this.measuredWidth,
            this.y.toInt() + this.measuredHeight
        )
    } else {
        this.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        this.layout(0, 0, this.measuredWidth, this.measuredHeight)
    }
    this.draw(canvas)
    return bitmap
}

/**
 * 保存图片到系统相册
 *
 * @param context
 * @param bmp
 */
private fun saveImageToGallery(context: Context, bmp: Bitmap?) {

    if (bmp == null) {
        throw IllegalStateException("Image resource error")
    } else {
        try {
            var child = context.opPackageName.split(".")
            val sdCardDir = File(Environment.getExternalStorageDirectory(), child[child.size - 1])
            if (!sdCardDir.exists()) {              //如果不存在，那就建立这个文件夹
                sdCardDir.mkdirs()
            }

            val fileName = System.currentTimeMillis().toString() + ".jpg"
            val file = File(sdCardDir, fileName)

            val fos = FileOutputStream(file)
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            fos.close()

            // 通知图库更新
            context.sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("file://" + file.path)
                )
            )
            (context.getString(R.string.vb_string_save_locality_success)).toast()
        } catch (e: java.lang.Exception) {
            e.logE()
            e.printStackTrace()
            (context.getString(R.string.vb_string_save_locality_error)).toast()

        }
    }


}


