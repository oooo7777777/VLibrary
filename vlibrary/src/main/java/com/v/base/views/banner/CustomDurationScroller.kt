package com.v.base.views.banner

import android.content.Context
import android.view.animation.Interpolator
import android.widget.Scroller

class CustomDurationScroller : Scroller {

    private var scrollFactor = 1.0

    constructor(context: Context) : super(context) {
    }

    constructor(context: Context, interpolator: Interpolator) : super(context, interpolator) {
    }

    /**
     * not exist in android 2.3
     *
     * @param context
     * @param interpolator
     * @param flywheel
     */
    // @SuppressLint("NewApi")
    // public CustomDurationScroller(Context context, Interpolator interpolator, boolean flywheel){
    // super(context, interpolator, flywheel);
    // }

    /**
     * Set the factor by which the duration will change
     */
    fun setScrollDurationFactor(scrollFactor: Double) {
        this.scrollFactor = scrollFactor
    }

    override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
        super.startScroll(startX, startY, dx, dy, (duration * scrollFactor).toInt())
    }
}
