package com.v.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter


//FragmentStatePagerAdapter构造函数behavior参数设置为BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
// 此种模式是官方推荐的模式,设置后,fragment可见和不可见只会走onResume和onPause函数,而不会走setUserVisibleHint
class VBFragmentAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val titles: Array<String>,
) : FragmentStatePagerAdapter(fm,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}