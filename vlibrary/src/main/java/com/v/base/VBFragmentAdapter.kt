package com.v.base

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter


@SuppressLint("WrongConstant")
class VBFragmentAdapter(
    fm: FragmentManager,
    private val list: List<Fragment>,
    private val titles: Array<String>
) : FragmentStatePagerAdapter(fm) {
    override fun getItem(arg0: Int): Fragment {
        return list[arg0]
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position]
    }
}
