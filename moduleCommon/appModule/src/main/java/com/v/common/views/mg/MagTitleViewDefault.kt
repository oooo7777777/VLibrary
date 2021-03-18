package  com.v.common.views.mg;

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

class MagTitleViewDefault(context: Context?) :
    ColorTransitionPagerTitleView(context)
{

    init
    {
        textSize = 18f
        normalColor = Color.parseColor("#666666")
        selectedColor = Color.parseColor("#121212")
    }

    private var listener: SelectListener? = null

    private var minScale = 0.9f

    override fun onEnter(
        index: Int,
        totalCount: Int,
        enterPercent: Float,
        leftToRight: Boolean
    )
    {
        super.onEnter(index, totalCount, enterPercent, leftToRight)
        listener?.onEnter(index)
        scaleX = minScale + (1.0f - minScale) * enterPercent
        scaleY = minScale + (1.0f - minScale) * enterPercent
        typeface = Typeface.defaultFromStyle(Typeface.BOLD);
    }

    override fun onLeave(
        index: Int,
        totalCount: Int,
        leavePercent: Float,
        leftToRight: Boolean
    )
    {
        super.onLeave(index, totalCount, leavePercent, leftToRight)
        listener?.onLeave(index)
        scaleX = 1.0f + (minScale - 1.0f) * leavePercent
        scaleY = 1.0f + (minScale - 1.0f) * leavePercent
        typeface = Typeface.defaultFromStyle(Typeface.NORMAL);
    }


    fun setSelectListener(listener: SelectListener)
    {
        this.listener = listener
    }

    interface SelectListener
    {
        fun onEnter(index: Int)

        fun onLeave(index: Int)
    }

}
