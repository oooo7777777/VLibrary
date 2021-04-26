package com.v.demo

import android.view.View
import com.v.base.BaseFragment
import com.v.base.utils.*
import com.v.base.utils.ext.*
import com.v.demo.databinding.FragmentTowBinding
import com.v.demo.model.DemoViewModel


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class TwoFragment : BaseFragment<FragmentTowBinding, DemoViewModel>(), View.OnClickListener {
    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {

    }

    override fun onClick(v: View) {
        when (v.id) {

            mViewBinding.bt0.id -> {
                "点击间隔默认500".toast()
            }
            mViewBinding.bt1.id -> {
                "点击间隔1000".toast()
            }
            mViewBinding.bt2.id -> {
                "显示toast".toast()
            }
            mViewBinding.bt3.id -> {
                "打印日志".logE()
                "打印日志".logV()
                "打印日志".logD()
                "打印日志".logI()
                "打印日志".logW()
                "打印日志".log()
            }
            mViewBinding.bt4.id -> {
                mViewBinding.bt4.setDrawable(R.mipmap.ic_movie, null, h = 50, w = 50)
            }
            mViewBinding.bt5.id -> {
                mViewBinding.bt5.setDrawable(null, R.mipmap.ic_movie, h = 80, w = 80)
            }
            mViewBinding.bt6.id -> {
                mViewBinding.ivIcon.load(R.mipmap.ic_movie, 90.randomNumber().toFloat())
            }
            mViewBinding.bt7.id -> {
                mViewBinding.ivIcon.loadCircle(R.mipmap.ic_movie)
            }

        }
    }
}