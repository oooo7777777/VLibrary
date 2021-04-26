package com.v.demo

import android.view.View
import com.v.base.BaseFragment
import com.v.demo.databinding.FragmentFourBinding
import com.v.demo.model.DataViewModel

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class FourFragment : BaseFragment<FragmentFourBinding, DataViewModel>(), View.OnClickListener {
    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {

        mViewModel.content.observe(this, androidx.lifecycle.Observer {
            mViewBinding.tvContent.text = it
        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            mViewBinding.bt0.id -> {
                mViewModel.setContent("我是FourFragment的数据~~~~~~~~~")
            }
        }
    }
}