package com.zlzw.home.debug

import com.v.base.BaseActivity
import com.v.base.BlankViewModel
import com.v.base.utils.ext.addFragment
import com.zlzw.home.HomeFragment
import com.zlzw.home.R
import com.zlzw.home.databinding.HmActivityBinding

class HomeActivity : BaseActivity<HmActivityBinding, BlankViewModel>()
{

    override fun initData()
    {
        this.addFragment(HomeFragment(),R.id.llRoot)
    }

    override fun createObserver() {

    }
}