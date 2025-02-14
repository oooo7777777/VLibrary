package com.v.base.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.nio.ByteBuffer
import java.security.MessageDigest



class RoundedCornersBorderTransformation(
    private val topLeft: Float,
    private val topRight: Float,
    private val bottomLeft: Float,
    private val bottomRight: Float,
    private val borderColor: Int,
    private val borderWidth: Float
) : BitmapTransformation() {

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888).apply {
            eraseColor(Color.TRANSPARENT)
        }

        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isFilterBitmap = true
            isDither = true
        }

        // 计算用于绘制边框的区域
        val borderInset = borderWidth / 2
        val borderRect = RectF(borderInset, borderInset, width - borderInset, height - borderInset)

        // 定义每个角的圆角半径
        val radii = floatArrayOf(
            topLeft, topLeft,      // 左上角
            topRight, topRight,    // 右上角
            bottomRight, bottomRight, // 右下角
            bottomLeft, bottomLeft   // 左下角
        )

        // 1. 裁剪圆角路径
        val clipPath = Path().apply {
            addRoundRect(borderRect, radii, Path.Direction.CW)
        }

        canvas.save()
        canvas.clipPath(clipPath)

        // 2. 绘制图片
        canvas.drawBitmap(toTransform, 0f, 0f, paint)
        canvas.restore()

        // 3. 绘制边框
        if (borderWidth > 0) {
            val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                color = borderColor
                strokeWidth = borderWidth
            }

            val borderPath = Path().apply {
                addRoundRect(borderRect, radii, Path.Direction.CW)
            }
            canvas.drawPath(borderPath, borderPaint)
        }

        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        val data = ByteBuffer.allocate(24)
            .putFloat(topLeft)
            .putFloat(topRight)
            .putFloat(bottomLeft)
            .putFloat(bottomRight)
            .putInt(borderColor)
            .putFloat(borderWidth)
            .array()
        messageDigest.update(data)
    }
}
