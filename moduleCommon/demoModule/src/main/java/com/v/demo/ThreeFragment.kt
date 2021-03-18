package com.v.demo

import android.view.View
import androidx.lifecycle.Observer
import com.v.base.BaseFragment
import com.v.base.utils.getApplicationViewModel
import com.v.demo.databinding.DmFragmentThreeBinding
import com.v.demo.model.AppViewModel
import com.v.demo.model.DataViewModel


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class ThreeFragment : BaseFragment<DmFragmentThreeBinding, DataViewModel>(), View.OnClickListener {

    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {

        mViewModel.content.observe(this, Observer {
            mViewBinding.tvContent.text = it
        })

        getApplicationViewModel(mContext.application, AppViewModel::class.java).userBane.observe(
            this,
            androidx.lifecycle.Observer {
                mViewBinding.userBean = it
            })
    }

    override fun onClick(v: View) {
        when (v.id) {
            mViewBinding.bt0.id -> {
                mViewModel.setContent("ViewModel 对象存在的时间范围是获取 ViewModel 时传递给 ViewModelProvider 的 Lifecycle。ViewModel 将一直留在内存中，直到限定其存在时间范围的 Lifecycle 永久消失：对于 Activity，是在 Activity 完成时；而对于 Fragment，是在 Fragment 分离时。")
            }
        }
    }

}