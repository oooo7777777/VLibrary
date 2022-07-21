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

import jp.wasabeef.glide.transformations.RoundedCornersTransformation


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
            mDataBinding.bt2.id -> {
                "打印日志".log()
            }
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