package com.v.demo

import android.graphics.Color
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.v.base.BaseFragment
import com.v.base.utils.ext.countDownCoroutines
import com.v.demo.databinding.FragmentFourBinding
import com.v.demo.model.DataViewModel
import kotlinx.coroutines.Job

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class FourFragment : BaseFragment<FragmentFourBinding, DataViewModel>(), View.OnClickListener {

    private var job: Job? = null
    private var countDown = 5L


    override fun initData() {
        mDataBinding.v = this
    }

    override fun createObserver() {

        mViewModel.content.observe(this, androidx.lifecycle.Observer {
            mDataBinding.tvContent.text = it
        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            mDataBinding.bt0.id -> {
                mViewModel.setContent("我是FourFragment的数据~~~~~~~~~")
            }
            mDataBinding.bt1.id -> {
                countDown = 5
                countDownStart()
            }
            mDataBinding.bt2.id -> {
                countDown = Long.MAX_VALUE
                countDownStart()
            }
        }
    }


    private fun countDownStart() {
        countDownStop()
        if (job == null) {
            job = countDownCoroutines(countDown, onTick = {
                mDataBinding.tvContent.text = it.toString()

            }, onFinish = {
                mDataBinding.tvContent.text = "倒计时结束"

            }, scope = lifecycleScope)
        }
    }

    private fun countDownStop() {
        if (job != null) {
            job?.cancel()
            job = null
        }
    }

}