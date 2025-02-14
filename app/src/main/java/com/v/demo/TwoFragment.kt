package com.v.demo

import android.graphics.Color
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
                mDataBinding.bt3.vbDrawable(
                    left = R.mipmap.bg_wechatimg11,
                    leftH = 30,
                    leftW = 30,
                    right = R.mipmap.bg_wechatimg11,
                    rightH = 50,
                    rightW = 50,
                    top = null,
                    w = 150,
                    h = 150
                )
            }

            mDataBinding.bt5.id -> {
                mDataBinding.ivIcon.vbLoad(
                    R.mipmap.bg_wechatimg11
                )
            }

            mDataBinding.bt6.id -> {
                mDataBinding.ivIcon.vbLoad(
                    R.mipmap.bg_wechatimg11,
                    cornersRadius = 90.vbGetRandomNumber(),
                    strokeWidth = 5,
                    strokeColor = Color.RED
                )
            }

            mDataBinding.bt7.id -> {
                mDataBinding.ivIcon.vbLoad(
                    R.mipmap.bg_wechatimg11,
                    topLeft = 90.vbGetRandomNumber(),
                    topRight = 90.vbGetRandomNumber(),
                    bottomLeft = 90.vbGetRandomNumber(),
                    bottomRight = 90.vbGetRandomNumber(),
                    strokeWidth = 5,
                    strokeColor = Color.RED
                )
            }

            mDataBinding.bt8.id -> {
                mDataBinding.ivIcon.vbLoad(
                    R.mipmap.bg_wechatimg11,
                    isCircle = true,
                    strokeWidth = 5,
                    strokeColor = Color.RED,
                )
            }
        }
    }
}