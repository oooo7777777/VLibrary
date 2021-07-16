package com.v.demo

import com.v.base.VBBlankViewModel
import com.v.base.utils.ext.vbDataBinding
import com.v.base.utils.toast
import com.v.demo.databinding.ActivityTestBinding

/**
 * @Author : ww
 * desc    :
 * time    : 2021/7/16 14:16
 */
class TestActivity : CameraActivity<ActivityTestBinding,VBBlankViewModel>() {

    override fun toolBarTitle(title: String, titleColor: Int) {
        super.toolBarTitle("title", titleColor)
    }

    override fun start() {
        "来了老弟".toast()
    }

    override fun createObserver() {
    }


}