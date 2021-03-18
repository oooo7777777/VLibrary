package com.zlzw.me

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.v.common.RouterConstant
import com.v.base.BaseFragment
import com.v.base.utils.log
import com.zlzw.me.databinding.MeFragmentBinding
import com.zlzw.me.model.MeViewModel


@Route(path = RouterConstant.ROUTER_FRAGMENT_ME)
class MeFragment : BaseFragment<MeFragmentBinding, MeViewModel>(), View.OnClickListener {


    override fun initData() {
        mViewBinding.v = this
    }


    override fun createObserver() {
    }


    override fun onClick(v: View) {

        when (v.id) {
            R.id.bt1 -> {
              mViewModel.login("21312","2132")
            }
            R.id.bt2 -> {
                "onClick".log()
            }
            R.id.bt3 -> {
                "onClick".log()
            }
        }

    }
}