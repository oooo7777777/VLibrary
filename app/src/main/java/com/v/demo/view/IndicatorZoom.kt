package  com.v.demo.view;

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.v.base.VBFragmentAdapter
import com.v.demo.R
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView
import java.util.ArrayList

/**
 * @Author : ww
 * desc    : 缩放
 * time    : 2021/1/11 15:44
 */
class IndicatorZoom(
    context: Context,
    viewPager: ViewPager,
    fragments: ArrayList<Fragment>,
    titles: Array<String>,
    iconOffs: Array<Int>,
    iconOns: Array<Int>
) :
    CommonNavigator(context) {

    init {

        viewPager.offscreenPageLimit = fragments.size

        if (context is AppCompatActivity) {
            val fragmentAdapter =
                VBFragmentAdapter(context.supportFragmentManager, fragments, titles)
            viewPager.adapter = fragmentAdapter

        } else if (context is Fragment) {

            context.childFragmentManager
            val fragmentAdapter =
                VBFragmentAdapter(context.childFragmentManager, fragments, titles)
            viewPager.adapter = fragmentAdapter
        }


        isAdjustMode = true
        adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return if (titles.isEmpty()) 0 else titles.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView? {

                val commonPagerTitleView = CommonPagerTitleView(context)

                val customLayout: View =
                    LayoutInflater.from(context).inflate(R.layout.view_tab, null)
                val titleImg = customLayout.findViewById<View>(R.id.ivIcon) as ImageView
                val titleText = customLayout.findViewById<View>(R.id.ivTitle) as TextView
                titleText.text = titles[index]
                titleImg.setImageResource(iconOffs[index])
                commonPagerTitleView.setContentView(customLayout)
                commonPagerTitleView.onPagerTitleChangeListener = object :
                    CommonPagerTitleView.OnPagerTitleChangeListener {
                    override fun onSelected(index: Int, totalCount: Int) {
                        titleText.setTextColor(Color.parseColor("#FF5369"))
                        titleImg.setImageResource(iconOns[index])
                    }

                    override fun onDeselected(index: Int, totalCount: Int) {
                        titleText.setTextColor(Color.parseColor("#333333"))
                        titleImg.setImageResource(iconOffs[index])
                    }

                    override fun onLeave(
                        index: Int,
                        totalCount: Int,
                        leavePercent: Float,
                        leftToRight: Boolean
                    ) {
                        titleImg.scaleX = 1.3f + (0.8f - 1.3f) * leavePercent
                        titleImg.scaleY = 1.3f + (0.8f - 1.3f) * leavePercent
                    }

                    override fun onEnter(
                        index: Int,
                        totalCount: Int,
                        enterPercent: Float,
                        leftToRight: Boolean
                    ) {
                        titleImg.scaleX = 0.8f + (1.3f - 0.8f) * enterPercent
                        titleImg.scaleY = 0.8f + (1.3f - 0.8f) * enterPercent
                    }
                }

                commonPagerTitleView.setOnClickListener {
                    viewPager.currentItem = index;
                }
                return commonPagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
                return null
            }
        }
    }


}
