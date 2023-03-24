package com.v.demo

import androidx.fragment.app.Fragment
import com.v.base.VBActivity
import com.v.base.VBBlankViewModel
import com.v.base.utils.vbGetFragment
import com.v.demo.databinding.MainActivityBinding
import com.v.demo.view.IndicatorZoom
import com.v.log.util.log
import net.lucode.hackware.magicindicator.ViewPagerHelper

class MainActivity : VBActivity<MainActivityBinding, VBBlankViewModel>() {


    private val commonNavigator by lazy {

        val titles = resources.getStringArray(R.array.dm_tab)
        val iconOffs = arrayOf(
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
        )
        val iconOns = arrayOf(
            R.mipmap.ic_launcher_round,
            R.mipmap.ic_launcher_round,
            R.mipmap.ic_launcher_round
        )

        val fragments = ArrayList<Fragment>()
        fragments.add(vbGetFragment("home", OneFragment::class.java))
        fragments.add(vbGetFragment("home1", TwoFragment::class.java))
        fragments.add(vbGetFragment("home2", ThreeFragment::class.java))

        IndicatorZoom(
            this,
            mDataBinding.viewPager,
            fragments,
            titles,
            iconOffs,
            iconOns
        )

    }

    override fun useTranslucent(): Boolean {
        return true
    }

    override fun initData() {
        mDataBinding.v = this
        initMg()
    }

    override fun createObserver() {
    }


    private fun initMg() {
        mDataBinding.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(mDataBinding.magicIndicator, mDataBinding.viewPager);
        mDataBinding.viewPager.currentItem = 0
    }

}