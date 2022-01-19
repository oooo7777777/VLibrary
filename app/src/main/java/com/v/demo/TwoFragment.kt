package com.v.demo

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.v.base.VBFragment
import com.v.base.utils.*
import com.v.base.utils.ext.*
import com.v.demo.databinding.FragmentTowBinding
import com.v.demo.model.DemoViewModel
import kotlinx.coroutines.launch


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
                mDataBinding.bt4.vbDrawable(left = R.mipmap.ic_movie, leftH = 30, leftW = 30,
                    right = R.mipmap.ic_movie, rightH = 50, rightW = 50,top = null,w = 150,h = 150)
            }
            mDataBinding.bt5.id -> {
                mDataBinding.bt5.vbDrawable(right = R.mipmap.ic_movie, h = 80, w = 80)
            }
            mDataBinding.bt6.id -> {
                mDataBinding.ivIcon.vbLoad(R.mipmap.ic_movie, 90.vbGetRandomNumber().toFloat())
            }
            mDataBinding.bt7.id -> {
                mDataBinding.ivIcon.vbLoadCircle(R.mipmap.ic_movie)
            }
            mDataBinding.bt8.id -> {
                mDataBinding.scrollView.vbSaveLocality(mContext)
            }
            mDataBinding.bt9.id -> {
                "http://gank.io/images/9fb66f5c4a214b26be6e0218b93bdf46".vbSaveLocality(mContext)
            }
            mDataBinding.bt10.id -> {
                val bitmap = BitmapFactory.decodeResource(mContext.resources, R.mipmap.ic_movie)
                bitmap.vbSaveLocality(mContext)
            }

        }
    }
}