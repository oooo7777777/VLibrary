package com.v.demo

import android.view.View
import com.v.base.VBFragment
import com.v.base.utils.*
import com.v.demo.databinding.FragmentTowBinding
import com.v.demo.viewmodel.OneViewModel


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class TwoFragment : VBFragment<FragmentTowBinding, OneViewModel>(), View.OnClickListener {
    override fun initData() {
        mDataBinding.v = this
    }
    override fun createObserver() {

    }
    override fun onClick(v: View) {
        when (v.id) {
            mDataBinding.bt3.id -> {
                mDataBinding.bt3.vbDrawable(left = R.mipmap.ic_movie,
                    leftH = 30,
                    leftW = 30,
                    right = R.mipmap.ic_movie,
                    rightH = 50,
                    rightW = 50,
                    top = null,
                    w = 150,
                    h = 150)
            }
            mDataBinding.bt4.id -> {
                mDataBinding.bt4.vbDrawable(right = R.mipmap.ic_movie, h = 80, w = 80)
            }
            mDataBinding.bt5.id -> {
                mDataBinding.ivIcon.vbLoad(R.mipmap.ic_movie, 90.vbGetRandomNumber())
            }
            mDataBinding.bt6.id -> {
                mDataBinding.ivIcon.vbLoadCircle(R.mipmap.ic_movie)
            }
            mDataBinding.bt7.id -> {
                mDataBinding.ivIcon.vbLoadRounded(R.mipmap.ic_movie, 0, 0, 30, 40)
            }
        }
    }
}