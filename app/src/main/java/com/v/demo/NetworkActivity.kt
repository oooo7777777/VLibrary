package com.v.demo

import com.v.base.BaseActivity
import android.view.View
import androidx.lifecycle.Observer
import com.v.base.utils.linear
import com.v.base.utils.loadData
import com.v.base.utils.toJson
import com.v.demo.adapter.NetworkActivityAdapter
import com.v.demo.model.NetworkViewModel
import com.v.demo.databinding.ActivityNetworkBinding
import com.v.demo.R
import com.v.demo.bean.GirlBean

/**
 * author  :
 * desc    :  网络请求演示
 * time    : 2021-03-20 11:47:48
 */
class NetworkActivity : BaseActivity<ActivityNetworkBinding, NetworkViewModel>(),
    View.OnClickListener {

    override fun toolBarTitle(title: String, titleColor: Int) {
        super.toolBarTitle(this.getString(R.string.string_activity_network_title), titleColor)
    }

    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {
        mViewModel.string.observe(this, Observer {
            val string = it
            mViewBinding.tvContent.text = string
        })
    }

    override fun onClick(v: View) {
        when (v.id) {
            mViewBinding.bt0.id -> {
                mViewModel.getCustom()
            }

            mViewBinding.bt1.id -> {
                mViewModel.getCustomDefault()
            }

            mViewBinding.bt2.id -> {
                mViewModel.getBase()
            }


        }
    }

}