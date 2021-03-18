package com.v.common.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import com.scwang.smart.refresh.layout.api.RefreshHeader
import com.scwang.smart.refresh.layout.api.RefreshKernel
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.constant.RefreshState
import com.scwang.smart.refresh.layout.constant.SpinnerStyle
import com.v.app.R

class ClassicsHeader : LinearLayout, RefreshHeader {
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.app_header, this, true)
    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        return SpinnerStyle.Translate
    }

    override fun onStartAnimator(layout: RefreshLayout, headHeight: Int, maxDragHeight: Int) {}
    override fun onFinish(layout: RefreshLayout, success: Boolean): Int {
        return 0
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
    }

    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {}
    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {}
    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {}
    override fun setPrimaryColors(@ColorInt vararg colors: Int) {}
}