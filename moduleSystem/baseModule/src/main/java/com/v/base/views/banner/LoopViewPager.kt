package com.v.base.views.banner

import android.content.Context
import android.os.Handler
import android.os.Message
import androidx.core.view.MotionEventCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.Interpolator
import androidx.viewpager.widget.ViewPager
import java.lang.ref.WeakReference

class LoopViewPager : ViewPager
{

    /** auto scroll time in milliseconds, default is [.DEFAULT_INTERVAL]  */
    /**
     * get auto scroll time in milliseconds, default is [.DEFAULT_INTERVAL]
     *
     * @return the interval
     */
    /**
     * set auto scroll time in milliseconds, default is [.DEFAULT_INTERVAL]
     *
     * @param interval the interval to set
     */
    var interval = DEFAULT_INTERVAL.toLong()
    /** auto scroll direction, default is [.RIGHT]  */
    private var direction = RIGHT
    /** whether automatic cycle when auto scroll reaching the last or first item, default is true  */
    /**
     * whether automatic cycle when auto scroll reaching the last or first item, default is true
     *
     * @return the isCycle
     */
    /**
     * set whether automatic cycle when auto scroll reaching the last or first item, default is true
     *
     * @param isCycle the isCycle to set
     */
    var isCycle = true
    var isStopScrollWhenTouch = true
    var isBorderAnimation = true
    /** scroll factor for auto scroll animation, default is 1.0  */
    private var autoScrollFactor = 1.0
    /** scroll factor for swipe scroll animation, default is 1.0  */
    private var swipeScrollFactor = 1.0

    private var mHandler: Handler? = null
    private var isAutoScroll = false
    private var isStopByTouch = false

    private var scroller: CustomDurationScroller? = null

    constructor(paramContext: Context) : super(paramContext) {
        init()
    }

    constructor(paramContext: Context, paramAttributeSet: AttributeSet) : super(paramContext, paramAttributeSet) {
        init()
    }

    private fun init() {
        mHandler = MyHandler(this)
        setViewPagerScroller()
    }

    /**
     * start auto scroll, first scroll delay time is [.getInterval]
     */
    fun startAutoScroll() {
        isAutoScroll = true
        sendScrollMessage((interval + scroller!!.duration / autoScrollFactor * swipeScrollFactor).toLong())
    }

    /**
     * start auto scroll
     *
     * @param delayTimeInMills first scroll delay time
     */
    fun startAutoScroll(delayTimeInMills: Int) {
        isAutoScroll = true
        sendScrollMessage(delayTimeInMills.toLong())
    }

    /**
     * stop auto scroll
     */
    fun stopAutoScroll() {
        isAutoScroll = false
        mHandler!!.removeMessages(SCROLL_WHAT)
    }

    /**
     * set the factor by which the duration of sliding animation will change while swiping
     */
    fun setSwipeScrollDurationFactor(scrollFactor: Double) {
        swipeScrollFactor = scrollFactor
    }

    /**
     * set the factor by which the duration of sliding animation will change while auto scrolling
     */
    fun setAutoScrollDurationFactor(scrollFactor: Double) {
        autoScrollFactor = scrollFactor
    }

    private fun sendScrollMessage(delayTimeInMills: Long) {
        /** remove messages before, keeps one message is running at most  */
        mHandler!!.removeMessages(SCROLL_WHAT)
        mHandler!!.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills)
    }

    /**
     * set ViewPager scroller to change animation duration when sliding
     */
    private fun setViewPagerScroller() {
        try {
            val scrollerField = ViewPager::class.java.getDeclaredField("mScroller")
            scrollerField.isAccessible = true
            val interpolatorField = ViewPager::class.java.getDeclaredField("sInterpolator")
            interpolatorField.isAccessible = true

            scroller = CustomDurationScroller(context, interpolatorField.get(null) as Interpolator)
            scrollerField.set(this, scroller)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * scroll only once
     */
    fun scrollOnce() {
        val adapter = adapter
        var currentItem = currentItem
        var totalCount =adapter!!.count
        if (adapter == null || totalCount <= 1) {
            return
        }

        val nextItem = if (direction == LEFT) --currentItem else ++currentItem
        if (nextItem < 0) {
            if (isCycle) {
                setCurrentItem(totalCount - 1, isBorderAnimation)
            }
        } else if (nextItem == totalCount) {
            if (isCycle) {
                setCurrentItem(0, isBorderAnimation)
            }
        } else {
            setCurrentItem(nextItem, true)
        }
    }

    /**
     *
     * if stopScrollWhenTouch is true
     *  * if event is down, stop auto scroll.
     *  * if event is up, start auto scroll again.
     *
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean
    {
        val action = MotionEventCompat.getActionMasked(ev)
        if (isStopScrollWhenTouch)
        {
            if (action == MotionEvent.ACTION_DOWN && isAutoScroll)
            {
                isStopByTouch = true
                stopAutoScroll()
            } else if (ev.action == MotionEvent.ACTION_UP && isStopByTouch)
            {
                startAutoScroll()
            }
        }
        //getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev)
    }

    private class MyHandler(autoScrollViewPager: LoopViewPager) : Handler() {

        private val autoScrollViewPager: WeakReference<LoopViewPager>

        init {
            this.autoScrollViewPager = WeakReference(autoScrollViewPager)
        }

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            when (msg.what) {
                SCROLL_WHAT -> {
                    val pager = this.autoScrollViewPager.get()
                    if (pager != null) {
                        pager.scroller!!.setScrollDurationFactor(pager.autoScrollFactor)
                        pager.scrollOnce()
                        pager.scroller!!.setScrollDurationFactor(pager.swipeScrollFactor)
                        pager.sendScrollMessage(pager.interval + pager.scroller!!.duration)
                    }
                }
                else -> {
                }
            }
        }
    }

    /**
     * get auto scroll direction
     *
     * @return [.LEFT] or [.RIGHT], default is [.RIGHT]
     */
    fun getDirection(): Int {
        return if (direction == LEFT) LEFT else RIGHT
    }

    /**
     * set auto scroll direction
     *
     * @param direction [.LEFT] or [.RIGHT], default is [.RIGHT]
     */
    fun setDirection(direction: Int) {
        this.direction = direction
    }

    companion object {

        val DEFAULT_INTERVAL = 2500

        val LEFT = 0
        val RIGHT = 1

        /** do nothing when sliding at the last or first item  */
        val SLIDE_BORDER_MODE_NONE = 0
        /** cycle when sliding at the last or first item  */
        val SLIDE_BORDER_MODE_CYCLE = 1
        /** deliver event to parent when sliding at the last or first item  */
        val SLIDE_BORDER_MODE_TO_PARENT = 2

        val SCROLL_WHAT = 0
    }
}