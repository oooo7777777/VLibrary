package com.v.demo

import android.view.View
import com.v.base.VBFragment
import com.v.base.utils.*
import com.v.demo.databinding.FragmentTowBinding
import com.v.demo.model.DemoViewModel
import com.bumptech.glide.request.RequestOptions

import android.graphics.Bitmap
import android.graphics.drawable.Drawable

import com.bumptech.glide.Glide

import com.bumptech.glide.load.resource.bitmap.CenterCrop

import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.target.Target
import com.v.demo.databinding.FragmentFourBinding
import com.v.log.util.log

import jp.wasabeef.glide.transformations.RoundedCornersTransformation


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class FourFragment : VBFragment<FragmentFourBinding, DemoViewModel>(), View.OnClickListener {

    private var num = 1
    override fun initData() {
        mDataBinding.v = this
        mDataBinding.dashBoard.setDefaultData(num)
    }

    override fun createObserver() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            mDataBinding.bt0.id -> {
                mDataBinding.dashBoard.setData(0, 160)
            }
            mDataBinding.bt1.id -> {
                mDataBinding.dashBoard.setData(30, 80)
            }
            mDataBinding.bt2.id -> {
                mDataBinding.dashBoard.setInterval(55, 80)
            }
            mDataBinding.bt3.id -> {
                mDataBinding.dashBoard.setData(10, 150)
            }
            mDataBinding.bt4.id -> {
                mDataBinding.dashBoard.setInterval(40, 60)
            }
            mDataBinding.bt5.id -> {
                mDataBinding.dashBoard.setProgress(0)
            }
            mDataBinding.bt6.id -> {
                mDataBinding.dashBoard.setProgress(10)
            }
            mDataBinding.bt7.id -> {
                mDataBinding.dashBoard.setProgress(20)
            }
            mDataBinding.bt8.id -> {
                mDataBinding.dashBoard.setProgress(35)
            }
            mDataBinding.bt9.id -> {
                mDataBinding.dashBoard.setProgress(80)
            }
            mDataBinding.bt10.id -> {
                mDataBinding.dashBoard.setProgress(200)
            }

            mDataBinding.bt12.id -> {
                vbCountDownCoroutines(onTick = {
                    val p = vbGetRandomNumber(0, 220)
                    mDataBinding.dashBoard.setProgress(p)
                })
            }

        }
    }
}