package com.v.base.utils


import android.graphics.Rect
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import com.v.base.utils.ext.logE
import com.v.base.utils.ext.vbInvalidClick
import kotlin.math.roundToInt

/**
 * @Author : ww
 * desc    : 点击动画
 * time    : 2020/12/29 15:22
 */

class ViewClickAnimatorUtil(
    var view: View,
    var clickTime: Long = 500L,
    var onClick: ((v: View) -> Unit)

) {
    private var down = false
    private val timeAnim = 150L

    init {
        addTouchListener()
    }

    private fun getF(): Float {
        return 1.0f - TypedValue.applyDimension(
            1,
            10.0f,
            view.context.resources.displayMetrics
        ).roundToInt().toFloat() / view.width.toFloat()
    }

    private fun addTouchListener() {

        view.setOnTouchListener { v, event ->

            var animation: ScaleAnimation? = null
            val viewRect = Rect()
            this.view.getLocalVisibleRect(viewRect)
            val b =
                event.x < viewRect.right.toFloat() && event.x > viewRect.left.toFloat() && event.y < viewRect.bottom.toFloat() && event.y > viewRect.top.toFloat()
            when (event.action) {
                MotionEvent.ACTION_DOWN -> if (!this.down) {
                    animation =
                        ScaleAnimation(1.0f, this.getF(), 1.0f, this.getF(), 1, 0.5f, 1, 0.5f)
                    animation.duration = timeAnim
                    animation.fillAfter = true
                    this.view.startAnimation(animation)

                    this.down = true
                    this.view.isPressed = true
                }
                MotionEvent.ACTION_UP -> this.clearAnimation(animation, b, true)
                MotionEvent.ACTION_MOVE -> if (!b) {
                    this.clearAnimation(animation, b, false)
                }
                MotionEvent.ACTION_CANCEL -> this.clearAnimation(animation, b, false)
                else -> this.clearAnimation(animation, b, false)
            }
            true
        }

    }

    private fun clearAnimation(scaleAnimation: ScaleAnimation?, b: Boolean, up: Boolean) {
        try {

            var animation = scaleAnimation
            this.view.isPressed = false
            if (this.down) {
                this.down = false
                animation =
                    ScaleAnimation(this.getF(), 1.0f, this.getF(), 1.0f, 1, 0.5f, 1, 0.5f)
                animation.duration = timeAnim
                if (up) {

                    animation.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(paramAnimation: Animation) {
//                            ("onAnimationStart" ).log()
//                            if (!isInvalidClick(view, clickTime) && b) {
//                                onClick(view)
//                            }

                        }

                        override fun onAnimationRepeat(paramAnimation: Animation) {
//                            ("onAnimationRepeat" ).log()
                        }

                        override fun onAnimationEnd(paramAnimation: Animation) {

//                            ("onAnimationEnd" ).log()

                            if (!view.vbInvalidClick(clickTime) && b) {
                                onClick(view)
                            }

                        }
                    })
                }
            }

            this.view.startAnimation(animation)
        } catch (e: Exception) {
            e.printStackTrace()
            e.toString().logE()
        }
    }


}