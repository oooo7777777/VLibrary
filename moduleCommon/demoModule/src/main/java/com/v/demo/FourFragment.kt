package com.v.demo

//import com.v.base.utils.ext.getAppViewModel

import android.view.View
import com.v.base.BaseFragment
import com.v.base.utils.ext.getApplicationViewModel
import com.v.demo.databinding.DmFragmentFourBinding
import com.v.demo.model.AppViewModel
import com.v.demo.model.DataViewModel

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class FourFragment : BaseFragment<DmFragmentFourBinding, DataViewModel>(), View.OnClickListener {
    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {

        mViewModel.content.observe(this, androidx.lifecycle.Observer {
            mViewBinding.tvContent.text = it
        })



        getApplicationViewModel(mContext.application, AppViewModel::class.java).userBane.observe(
            this,
            androidx.lifecycle.Observer {
                mViewBinding.userBean = it
            })


//        LiveDataBus.with<String>("Key").postData("")
//        LiveDataBus.with<String>("Key").observe(this, Observer {
//
//
//        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.bt0 -> {
                mViewModel.setContent("我是FourFragment的数据~~~~~~~~~")
            }
        }
    }
}