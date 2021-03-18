package com.v.base.views

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager


class CustomViewPager : ViewPager {

    private val isCanScroll = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    override fun onInterceptTouchEvent(arg0: MotionEvent): Boolean {
        return isCanScroll && super.onInterceptTouchEvent(arg0)
    }

    override fun onTouchEvent(arg0: MotionEvent): Boolean {
        return isCanScroll && super.onTouchEvent(arg0)
    }


    override fun setCurrentItem(item: Int, smoothScroll: Boolean) {
        super.setCurrentItem(item, smoothScroll)
    }

    override fun setCurrentItem(item: Int) {
        if (childCount > 2) {
            //去除页面切换时的滑动翻页效果
            super.setCurrentItem(item, false)
        } else {
            super.setCurrentItem(item, true)
        }
    }
}