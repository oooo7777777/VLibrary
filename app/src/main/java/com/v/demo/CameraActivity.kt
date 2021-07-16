package com.v.demo

import androidx.databinding.ViewDataBinding
import com.v.base.VBActivity
import com.v.base.VBViewModel
import com.v.base.utils.ext.vbDataBinding
import com.v.demo.databinding.ActivityCameraBinding


/**
 * author  :
 * desc    :  摄像头基类
 * time    : 2021-03-20 11:47:48
 */
abstract class CameraActivity<CB : ViewDataBinding, CVM : VBViewModel> : VBActivity<CB, CVM>() {

    override fun useAddViewVBRoot(): Boolean {
        return false
    }

    private val nb by lazy {
        vbDataBinding(R.layout.activity_camera) as ActivityCameraBinding
    }

    override fun initData() {

        nb.flRoot.addView(mDataBinding.root)
        mRootDataBinding.layoutContent.addView(nb.root)

        start()
    }


    protected abstract fun start()


}