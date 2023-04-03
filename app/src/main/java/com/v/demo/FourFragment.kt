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
        mDataBinding.dashBoard.setInterval(40, 80)
    }

    override fun createObserver() {
//        mDataBinding.dashBoard.setData(0, 160)
    }

    override fun onClick(v: View) {
        when (v.id) {
            mDataBinding.bt0.id -> {
                mDataBinding.dashBoard.setData(0, 160)
                mDataBinding.dashBoard.setInterval(140, 160)
            }
            mDataBinding.bt1.id -> {
                mDataBinding.dashBoard.setData(21, 139)
                mDataBinding.dashBoard.setInterval(101, 120)
            }
            mDataBinding.bt2.id -> {
                mDataBinding.dashBoard.setData(0, 141)
                mDataBinding.dashBoard.setInterval(0, 40)
            }
            mDataBinding.bt3.id -> {
                mDataBinding.dashBoard.setData(35, 160)
                mDataBinding.dashBoard.setInterval(80, 160)
            }
            mDataBinding.bt4.id -> {
                mDataBinding.dashBoard.setInterval(40, 80)
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
                mDataBinding.dashBoard.setProgress(100)
            }
            mDataBinding.bt11.id -> {
                mDataBinding.dashBoard.setProgress(140)
            }
            mDataBinding.bt12.id -> {
                mDataBinding.dashBoard.setProgress(150)
            }
            mDataBinding.bt13.id -> {
                mDataBinding.dashBoard.setProgress(160)
            }
            mDataBinding.bt14.id -> {
                mDataBinding.dashBoard.setProgress(200)
            }
            mDataBinding.bt15.id -> {
                mDataBinding.dashBoard.setProgress(-200)
            }
            mDataBinding.bt16.id -> {
                vbCountDownCoroutines(onTick = {
                    val p = vbGetRandomNumber(0, 200)
                    mDataBinding.dashBoard.setProgress(p)
                })
            }

        }
    }
}