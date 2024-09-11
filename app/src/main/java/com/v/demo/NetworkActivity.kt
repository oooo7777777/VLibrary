package com.v.demo

import android.view.View
import androidx.lifecycle.Observer
import com.v.base.VBActivity
import com.v.demo.databinding.ActivityNetworkBinding
import com.v.demo.viewmodel.NetworkViewModel

/**
 * author  :
 * desc    :  网络请求演示
 * time    : 2021-03-20 11:47:48
 */
class NetworkActivity : VBActivity<ActivityNetworkBinding, NetworkViewModel>(),
    View.OnClickListener {


    override fun toolBarTitle(
        title: String,
        titleColor: Int,
        isShowBottomLine: Boolean,
        resLeft: Int,
        listenerLeft: View.OnClickListener?,
    ): Boolean {
        return super.toolBarTitle(
            this.getString(R.string.string_activity_network_title),
            titleColor,
            isShowBottomLine,
            resLeft,
            listenerLeft
        )
    }

    override fun initData() {
        mDataBinding.v = this
    }

    override fun createObserver() {
        mViewModel.data1.observe(this) {
            mDataBinding.tvContent.text = it.toString()
        }
        mViewModel.data2.observe(this) {
            mDataBinding.tvContent.text = it.toString()
        }
        mViewModel.data3.observe(this) {
            mDataBinding.tvContent.text = it.toString()
        }
        mViewModel.data4.observe(this) {
            mDataBinding.tvContent.text = it.toString()
        }
        mViewModel.data5.observe(this) {
            mDataBinding.tvContent.text = it.toString()
        }
    }

    override fun onClick(v: View) {
        mDataBinding.tvContent.text = ""
        when (v.id) {
            mDataBinding.bt1.id -> {
                mViewModel.getData1()
            }

            mDataBinding.bt2.id -> {
                mViewModel.getData2()
            }

            mDataBinding.bt3.id -> {
                mViewModel.getData3()
            }

            mDataBinding.bt4.id -> {
                mViewModel.getData4()
            }

            mDataBinding.bt5.id -> {
                mViewModel.getData5()
            }
        }
    }
}