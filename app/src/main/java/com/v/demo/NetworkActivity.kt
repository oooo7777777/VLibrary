package com.v.demo

import android.view.View
import androidx.lifecycle.Observer
import com.v.base.VBActivity
import com.v.demo.databinding.ActivityNetworkBinding
import com.v.demo.model.NetworkViewModel

/**
 * author  :
 * desc    :  网络请求演示
 * time    : 2021-03-20 11:47:48
 */
class NetworkActivity : VBActivity<ActivityNetworkBinding, NetworkViewModel>(),
    View.OnClickListener {

    override fun toolBarTitle(title: String, titleColor: Int) {
        super.toolBarTitle(this.getString(R.string.string_activity_network_title), titleColor)
    }

    override fun initData() {
        mDataBinding.v = this
    }

    override fun createObserver() {
        mViewModel.string.observe(this, Observer {
            val string = it
            mDataBinding.tvContent.text = string
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            mDataBinding.bt0.id -> {
                mViewModel.getCustom()
            }

            mDataBinding.bt1.id -> {
                mViewModel.getCustomDefault()
            }

            mDataBinding.bt2.id -> {
                mViewModel.getBase()
            }


        }
    }

}