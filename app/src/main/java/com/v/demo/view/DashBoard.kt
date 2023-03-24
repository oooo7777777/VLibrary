package com.v.demo.view


import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.v.base.utils.vbDp2px2Float
import com.v.base.utils.vbSp2px2Float
import com.v.log.util.log
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * author  : ww
 * desc    : 仪表盘
 * time    : 2022/10/27 5:33 PM
 */

class DashBoard @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr), DefaultLifecycleObserver {


    //动画执行时间
    private var animDuration = 300L

    //显示文字
    private var unit = "rpm"

    //总刻度数
    private var totalCount = 10

    //图表最小刻度
    private var minNum = 0

    //图表最大刻度
    private var maxNum = 180

    //选中区间最小值
    private var minSelect = 0f

    //选中区间最大值
    private var maxSelect = 0f

    //每个间隔增加的数字
    private var itemNum = 0

    //整个圆盘角度
    private var totalRotate = 180f

    //选中的颜色块偏移角度
    private var selectRotate = 0f

    //开始的角度
    private var startRotate = 90f

    //结束的角度
    private var endRotate = 280f

    //刻度角度偏移
    private var scaleRotate = -90f

    //整个view宽度
    private var viewWidth = 0f

    //仪表盘半径
    private var length = 0f

    //进度百分比
    private var progress = 0f

    //变化前指针百分比
    private var perOld = 0f

    private var rect = RectF()

    //每一个绘制的刻度与的宽度
    private var tempRou = totalRotate / totalCount

    //变色环宽度
    private val paintColorWidth = 15.vbDp2px2Float(context)

    // 变色范围
    private var radioRadius = paintColorWidth / 1.3f

    //指针动画
    private var progressAnimator: ValueAnimator? = null


    //背景画笔
    private val paintBg by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            val colors = intArrayOf(
                Color.parseColor("#B3616161"),
                Color.parseColor("#00616161")
            )
            val position = floatArrayOf(0f, 1f)
            val linearGradient = LinearGradient(
                0f,
                -length,
                0f, 0f,
                colors, position,
                Shader.TileMode.CLAMP
            )
            shader = linearGradient
        }
    }


    //变色的圆环画笔
    private val paintColor by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = paintColorWidth
            style = Paint.Style.FILL
            val position = floatArrayOf(0f, 1f)
            val linearGradient = LinearGradient(
                0f,
                -length,
                0f, 0f,
                intArrayOf(
                    Color.parseColor("#B3616161"),
                    Color.parseColor("#00616161"),
                ), position,
                Shader.TileMode.CLAMP
            )
            shader = linearGradient
//            maskFilter =
//                BlurMaskFilter(radioRadius, BlurMaskFilter.Blur.OUTER)
        }
    }

    //选中区域画笔
    private val paintSelect by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = paintColorWidth
            color = Color.parseColor("#00FFFF")
        }
    }

    //刻度
    private val paintScale by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#99FFFFFF")
            strokeWidth = 5f
            strokeCap = Paint.Cap.ROUND
        }
    }

    //刻度文字
    private val paintScaleText by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#99FFFFFF")
            textSize = 11.vbSp2px2Float(context)
            textAlign = Paint.Align.CENTER
//            typeface = Typeface.createFromAsset(context!!.assets, "fonts/DINCondensedCRegular.ttf")
        }
    }

    //进度文字
    private val paintText by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 32.vbSp2px2Float(context)
            textAlign = Paint.Align.CENTER
        }
    }

    //三角指针
    private val paintPointer by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            style = Paint.Style.FILL

            maskFilter =
                BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = width / 2 + 30//加30 是往下偏移30度
        initIndex(width / 2)
        setMeasuredDimension(width, height)
    }

    private fun initIndex(specSize: Int) {
        viewWidth = specSize.toFloat()
        length = viewWidth / 4 * 3
        progress = 0f
        perOld = 0f
    }


    override fun onDraw(canvas: Canvas) {
        //绘制背景
        initBg(canvas)
        //刻度文字
        initScale(canvas)
        //绘制进度文字
        initText(canvas)
        //绘制指针
        initPointer(canvas)
    }


    //绘制背景
    private fun initBg(canvas: Canvas) {
        canvas.save()
        canvas.translate((canvas.width / 2).toFloat(), viewWidth)

        //背景
        canvas.drawCircle(0f, 0f, length, paintBg)
        canvas.drawCircle(0f, 0f, length - paintColorWidth, paintColor)

        //选中颜色快
        if (minSelect != 0f && maxSelect != 0f) {
            if (progress * 10f in minSelect..maxSelect) {
                paintSelect.color = Color.parseColor("#00FFFF")
            } else {
                paintSelect.color = Color.parseColor("#CDCDCD")
            }

            canvas.rotate(selectRotate, 0f, 0f)
            //每一个刻度/每个刻度间的间隔 得到每个间隔的具体度数
            val lw = tempRou / itemNum
            val startAngle = totalRotate + lw * (minSelect * 10f - minNum)
            val sweepAngle = lw * ((maxSelect - minSelect) * 10f)

//            70
//           val a = sweepAngle -((totalRotate/sweepAngle).roundToInt()*10f)

//            //整个圆盘角度
//            totalRotate = 160f
//            //选中的颜色块偏移角度
//            selectRotate = 30f
//            //开始的角度
//            startRotate = 100f
//            //结束的角度
//            endRotate = 270f
//            //刻度角度偏移
//            scaleRotate = -80f

            //90
//            val sweepAngle = 70f

            val margin = paintColorWidth / 2
            rect = RectF(
                -length + margin,
                -length + margin,
                length - margin,
                length - margin
            )
            canvas.drawArc(rect, startAngle, sweepAngle, false, paintSelect)
        }
    }

    //绘制刻度与刻度文字
    private fun initScale(canvas: Canvas) {

        canvas.restore()
        canvas.save()
        canvas.translate((canvas.width / 2).toFloat(), viewWidth)

        canvas.rotate(scaleRotate, 0f, 0f)

        if (minSelect == maxSelect) {
            return
        }
        val lw = tempRou / itemNum
        //绘制刻度和百分比
        for (i in minNum..maxNum) {
            if (i == (minSelect * 10).toInt() || i == (maxSelect * 10).toInt())
//            if (i % 10 == 0)
                            {
                canvas.drawText(
                    i.toString(),
                    0f,
                    -length + paintColorWidth * 2.5f,
                    paintScaleText
                )
                canvas.drawLine(
                    0f,
                    -length + paintColorWidth + 5f,
                    0f,
                    -length + paintColorWidth * 1.5f,
                    paintScale
                )
            }
            canvas.rotate(lw, 0f, 0f)
        }
    }

    //绘制提示文字
    private fun initText(canvas: Canvas) {
        canvas.restore()
        canvas.save()
        canvas.translate((canvas.width / 2).toFloat(), viewWidth)

        //进度
        val number = (progress * totalCount * 10).toInt()

        paintText.textSize = 32.vbSp2px2Float(context)
        canvas.drawText(number.toString(), 0f, -length / 3, paintText)

        paintText.textSize = 12.vbSp2px2Float(context)
        canvas.drawText(unit, 0f, -20f, paintText)
    }

    private fun initPointer(canvas: Canvas) {
        canvas.restore()
        canvas.save()
        canvas.translate((canvas.width / 2).toFloat(), viewWidth)

        var degrees =
            startRotate + ((progress * 100.0f - minNum) / (maxNum - minNum)) * totalRotate

        //如果最小的刻度是等于 则表示表盘是平的
        if (minNum<=0)
        {
            if (degrees > endRotate) {
                degrees = 270f
            }
        }
        else
        {
            if (degrees > endRotate) {
                degrees = endRotate
            }
        }


        if (degrees < startRotate) {
            degrees = startRotate - 10f / minNum * (minNum - progress * 100f)
        }

        //根据参数得到旋转角度
        canvas.rotate(degrees, 0f, 0f)

        val path = Path()
        path.moveTo(0f, length - 10)
        path.lineTo(-3f, length / 1.5f - paintColorWidth)
        path.lineTo(3f, length / 1.5f - paintColorWidth)
        path.lineTo(0f, length - 10)
        path.close()

        //绘制一个圆形到指针底部 使指针看起来像个圆角指针
        canvas.drawCircle(0f, length / 1.5f - paintColorWidth, 3f, paintPointer)
        canvas.drawPath(path, paintPointer)
    }


    /**
     * 设置进度
     */
    fun setProgress(per: Int) {
        setPer(per / (totalCount * 10f))
    }


    /**
     * 设置仪表盘最大最小值
     * @param minNum 最小刻度
     * @param maxNum 最大刻度
     */
    fun setData(minNum: Int, maxNum: Int) {
        "最小刻度:$minNum 最大刻度:$maxNum".log()

        //如果最小角度大于0  则要做角度倾斜
        if (minNum > 0) {
            //整个圆盘角度
            totalRotate = 160f
            //选中的颜色块偏移角度
            selectRotate = 30f
            //开始的角度
            startRotate = 100f
            //结束的角度
            endRotate = 270f
            //刻度角度偏移
            scaleRotate = -80f
        } else {
            //整个圆盘角度
            totalRotate = 180f
            //选中的颜色块偏移角度
            selectRotate = 0f
            //开始的角度
            startRotate = 90f
            //结束的角度
            endRotate = 280f
            //刻度角度偏移
            scaleRotate = -90f
        }
        tempRou = totalRotate / totalCount


        this.minNum = minNum
        this.maxNum = maxNum
        this.itemNum = ceil((maxNum - minNum) / totalCount.toDouble()).toInt()
        invalidate()
    }

    /**
     * 设置选中区间
     * @param minSelect 选中的最小刻度
     * @param maxSelect 选中的最大刻度
     */
    fun setInterval(minSelect: Int, maxSelect: Int) {
        "选中的最小刻度:$minSelect  选中的最大刻度:$maxSelect".log()
        this.minSelect = minSelect / 10f
        this.maxSelect = maxSelect / 10f
        invalidate()
    }

    private fun setPer(per: Float) {
        //如果传入的值 跟上一次的值相同则不响应 或者动画正在执行
        if (perOld == per || progressAnimator != null && progressAnimator!!.isRunning) {
            return
        }

        progressAnimator?.cancel()
        progressAnimator = ValueAnimator.ofFloat(perOld, per).apply {
            duration = animDuration
            addUpdateListener { animation ->
                progress = animation.animatedValue as Float
                invalidate()
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                }

                override fun onAnimationEnd(p0: Animator) {
                    perOld = per
                    invalidate()
                }

                override fun onAnimationCancel(p0: Animator) {

                }

                override fun onAnimationRepeat(p0: Animator) {
                }

            })
        }
        progressAnimator!!.start()
    }


    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        progressAnimator?.cancel()
    }

    /**
     * 通过设备类型 设置默认最大值最小值
     */
    fun setDefaultData(EQUIPMENT_ID: Int) {
        var min = 0
        var max = 0

        when (EQUIPMENT_ID) {
            //动感单车
            1 -> {
                min = 0
                max = 160
            }
            //椭圆机
            2 -> {
                min = 0
                max = 120
            }
            //划船机
            3 -> {
                min = 0
                max = 60
            }
        }
        setData(min, max)
        invalidate()
    }
}
