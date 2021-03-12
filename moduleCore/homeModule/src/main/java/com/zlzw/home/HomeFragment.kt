package com.zlzw.home

import com.alibaba.android.arouter.facade.annotation.Route
import com.ww.appmodule.RouterConstant
import com.v.base.BaseFragment
import com.v.base.BlankViewModel
import com.zlzw.home.databinding.HmFragmentBinding


@Route(path = RouterConstant.ROUTER_FRAGMENT_HOME)
class HomeFragment : BaseFragment<HmFragmentBinding, BlankViewModel>()
{
    override fun initData() {
    }

    override fun createObserver() {
    }


}