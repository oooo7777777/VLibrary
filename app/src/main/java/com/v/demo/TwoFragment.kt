package com.v.demo

import android.view.View
import com.v.base.VBFragment
import com.v.base.utils.*
import com.v.base.utils.ext.*
import com.v.demo.databinding.FragmentTowBinding
import com.v.demo.model.DemoViewModel


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class TwoFragment : VBFragment<FragmentTowBinding, DemoViewModel>(), View.OnClickListener {
    override fun initData() {
        mDataBinding.v = this
    }

    override fun createObserver() {

    }

    override fun onClick(v: View) {
        when (v.id) {

            mDataBinding.bt0.id -> {
                "点击间隔默认500".toast()
            }
            mDataBinding.bt1.id -> {
                "点击间隔1000".toast()
            }
            mDataBinding.bt2.id -> {
                "显示toast".toast()
            }
            mDataBinding.bt3.id -> {
                "打印日志".logE()
                "打印日志".logV()
                "打印日志".logD()
                "打印日志".logI()
                "打印日志".logW()
                "打印日志".log()
            }
            mDataBinding.bt4.id -> {
                mDataBinding.bt4.vbDrawable(R.mipmap.ic_movie, null, h = 50, w = 50)
            }
            mDataBinding.bt5.id -> {
                mDataBinding.bt5.vbDrawable(null, R.mipmap.ic_movie, h = 80, w = 80)
            }
            mDataBinding.bt6.id -> {
                mDataBinding.ivIcon.vbLoad(R.mipmap.ic_movie, 90.vbRandomNumber().toFloat())
            }
            mDataBinding.bt7.id -> {
                mDataBinding.ivIcon.vbLoadCircle(R.mipmap.ic_movie)
            }

        }
    }
}