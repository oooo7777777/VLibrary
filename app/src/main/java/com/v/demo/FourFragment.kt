package com.v.demo

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.v.base.VBBlankViewModel
import com.v.base.VBFragment
import com.v.base.utils.ext.vbCountDownCoroutines
import com.v.demo.databinding.FragmentFourBinding
import kotlinx.coroutines.Job

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class FourFragment : VBFragment<FragmentFourBinding, VBBlankViewModel>(), View.OnClickListener {

    private var job: Job? = null
    private var countDown = 5L


    override fun initData() {
        mDataBinding.v = this
    }

    override fun createObserver() {


    }

    override fun onClick(v: View) {
        when (v.id) {

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
            job = vbCountDownCoroutines(countDown, onTick = {
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