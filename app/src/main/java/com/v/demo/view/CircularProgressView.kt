package com.v.demo.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import com.v.base.utils.vbDp2px2Float
import com.v.base.utils.vbSp2px2Float

/**
 * author  : ww
 * desc    :
 * time    : 2023/8/4 16:56
 */
class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //是否显示整数
    private var isInt = false

    // 绘制区域
    private var mRectF: RectF? = null

    //圆环进度
    private var mProgress = 0f

    //圆形宽度
    private val progressWidth = 6.4.vbDp2px2Float()

    //最大进度
    private var mProgressMax = 100.0f

    // 背景圆环画笔
    private val mBackPaint by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
            isDither = true
            strokeWidth = progressWidth
            color = Color.parseColor("#33C0CCD1")
        }

    }

    // 进度圆环画笔
    private val mProgressPaint by lazy {
        Paint().apply {
            style = Paint.Style.STROKE
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
            isDither = true
            strokeWidth = progressWidth
            color = Color.BLUE
        }

    }

    // 定义头部画笔
    private val mProgressTopPaint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            isAntiAlias = true
            isDither = true
            strokeWidth = progressWidth
            color = Color.RED
        }

    };


    //进度文字
    private val mProgressPaintText by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 20.vbSp2px2Float()
            textAlign = Paint.Align.CENTER
        }
    }


    //最大文字
    private val mProgressMaxPaintText by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#848A9B")
            textSize = 13.vbSp2px2Float()
            textAlign = Paint.Align.CENTER
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val viewWide = measuredWidth - paddingLeft - paddingRight
        val viewHigh = measuredHeight - paddingTop - paddingBottom
        val mRectLength =
            ((if (viewWide > viewHigh) viewHigh else viewWide) - if (mBackPaint.strokeWidth > mProgressPaint.strokeWidth) mBackPaint.strokeWidth else mProgressPaint.strokeWidth).toInt()
        val mRectL = paddingLeft + (viewWide - mRectLength) / 2
        val mRectT = paddingTop + (viewHigh - mRectLength) / 2

        if (mRectF == null) {
            mRectF = RectF(
                mRectL.toFloat(),
                mRectT.toFloat(),
                (mRectL + mRectLength).toFloat(),
                (mRectT + mRectLength).toFloat()
            )
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val angle = 360 * mProgress / mProgressMax

        canvas.drawArc(mRectF!!, 0f, 360f, false, mBackPaint)
        canvas.drawArc(mRectF!!, 280f, angle, false, mProgressPaint)


        if (angle >= 360f) {
            canvas.drawCircle(
                width / 2f,
                mProgressTopPaint.strokeWidth / 2f,
                mProgressTopPaint.strokeWidth / 2,
                mProgressTopPaint
            )
        }

        var progress = mProgress.toString()
        var progressMax = mProgressMax.toString()
        if (isInt) {
            progress = mProgress.toInt().toString()
            progressMax = mProgressMax.toInt().toString()
        }
        canvas.drawText(
            progress,
            measuredWidth.toFloat() / 2,
            measuredWidth.toFloat() / 2,
            mProgressPaintText
        )

        canvas.drawText(
            progressMax,
            measuredWidth.toFloat() / 2,
            measuredWidth.toFloat() / 1.4f,
            mProgressMaxPaintText
        )
    }


    /**
     * 设置当前进度 获取当前进度
     * @param progress 当前进度
     */
    var progress: Float
        get() = mProgress
        set(progress) {
            mProgress = progress
            invalidate()
        }

    /**
     * 设置当前进度，并展示进度动画。如果动画时间小于等于0，则不展示动画
     * @param progress 当前进度（0-100）
     * @param animTime 动画时间（毫秒）
     */
    fun setProgress(progress: Float, animTime: Long) {
        if (progress == this.progress) {
            return
        }
        if (animTime <= 0) this.progress = progress else {
            val animator = ValueAnimator.ofFloat(mProgress, progress)
            animator.addUpdateListener { animation ->
                mProgress = animation.animatedValue as Float
                invalidate()
            }
            //            animator.setInterpolator(new OvershootInterpolator());
            animator.duration = animTime
            animator.start()
        }
    }

    /**
     * 设置最大值
     */
    fun setMaxProgress(progressMax: Float) {
        if (progressMax == mProgressMax) {
            return
        }
        this.mProgressMax = progressMax
        invalidate()
    }

    /**
     * 设置进度圆环颜色
     * @param colorArray 渐变色集合
     */
    private fun setProgressColor(colorArray: IntArray) {
        val sweepGradient = SweepGradient(
            width / 2f, height / 2f,
            colorArray, null
        )
        val matrix = Matrix()
        matrix.setRotate(-90f, width / 2f, height / 2f)
        sweepGradient.setLocalMatrix(matrix)

        mProgressPaint.shader = sweepGradient
        invalidate()
    }

    /**
     *  设置消耗样式
     */
    fun setCalories() {
        isInt = false
        val colors = intArrayOf(
            Color.parseColor("#FFAFAF"),
            Color.parseColor("#FE2550"),
        )
        mProgressTopPaint.color = colors.last()
        setProgressColor(colors)
    }

    /**
     *  设置跑步样式
     */
    fun setStepCount() {
        isInt = true
        val colors = intArrayOf(
            Color.parseColor("#FFDE73"),
            Color.parseColor("#FF803C")
        )
        mProgressTopPaint.color = colors.last()
        setProgressColor(colors)
    }

    /**
     *  设置时间样式
     */
    fun setTime() {
        isInt = true
        val colors = intArrayOf(
            Color.parseColor("#71FFB8"),
            Color.parseColor("#16D2E3")
        )
        mProgressTopPaint.color = colors.last()
        setProgressColor(colors)
    }

}