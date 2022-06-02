package com.v.base.utils


import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.math.roundToInt

/**
 * @Author : ww
 * desc    : 点击动画
 * time    : 2020/12/29 15:22
 */

class ViewClickAnimatorUtil(
    var view: View,
    var clickTime: Long = 500L,
    var onClick: ((v: View) -> Unit),

    ) : LifecycleObserver {
    private var down = false
    private val animDuration = 150L
    private var mAnimation: ScaleAnimation? = null

    // 上次点击时间
    private var mLastTime = 0L

    init {
        addTouchListener()
        view.context.vbLifecycleOwner()?.lifecycle?.addObserver(this)
    }

    private fun getF(): Float {
        return 1.0f - TypedValue.applyDimension(
            1,
            10.0f,
            view.context.resources.displayMetrics
        ).roundToInt().toFloat() / view.width.toFloat()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addTouchListener() {

        view.setOnTouchListener { _, event ->
            var animation: ScaleAnimation? = null
            val viewRect = Rect()
            this.view.getLocalVisibleRect(viewRect)
            val b =
                event.x < viewRect.right.toFloat() && event.x > viewRect.left.toFloat() && event.y < viewRect.bottom.toFloat() && event.y > viewRect.top.toFloat()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (!this.down) {
                    animation =
                        ScaleAnimation(1.0f, this.getF(), 1.0f, this.getF(), 1, 0.5f, 1, 0.5f)
                    animation.duration = animDuration
                    animation.fillAfter = true
                    this.view.startAnimation(animation)
                    this.down = true
                    this.view.isPressed = true
                }
                MotionEvent.ACTION_UP -> this.clearAnimation(true)
                MotionEvent.ACTION_MOVE -> if (!b) {
                    this.clearAnimation(false)
                }
                MotionEvent.ACTION_CANCEL -> this.clearAnimation(false)
                else -> this.clearAnimation(false)

            }
            true

        }

    }

    private fun clearAnimation(up: Boolean) {
        try {

            this.view.isPressed = false
            if (this.down) {
                this.down = false
                val animation =
                    ScaleAnimation(this.getF(), 1.0f, this.getF(), 1.0f, 1, 0.5f, 1, 0.5f)
                this.mAnimation = animation
                animation.duration = animDuration
                if (up) {
                    dispose()
                }
                this.view.startAnimation(animation)

            }


        } catch (e: Exception) {
            e.printStackTrace()
            e.toString().logE()
        }
    }


    private fun dispose() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastTime >= clickTime) {
            mLastTime = currentTime
            onClick(view)
        }
    }


}