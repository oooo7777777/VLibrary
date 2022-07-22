package com.v.demo

import android.view.View
import androidx.lifecycle.lifecycleScope
import com.v.base.VBBlankViewModel
import com.v.base.VBFragment
import com.v.base.dialog.VBHintDialog
import com.v.base.dialog.VBListDialog
import com.v.base.utils.goActivity
import com.v.base.utils.log
import com.v.base.utils.toast
import com.v.base.utils.vbCountDownCoroutines
import com.v.demo.databinding.FragmentThreeBinding
import kotlinx.coroutines.Job


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class ThreeFragment : VBFragment<FragmentThreeBinding, VBBlankViewModel>(), View.OnClickListener {


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

            mDataBinding.bt3.id -> {
                goActivity(NetworkActivity::class.java)
            }

            mDataBinding.bt4.id -> {
                VBHintDialog(mContext).setTitle("提示")
                        .setContent("确定保存吗?")
                        .setButtonText("取消", "确定")
                        .setClickListener { hintDialog, position ->
                            hintDialog.dismiss()
                        }
                        .show()
            }
            mDataBinding.bt5.id -> {
                VBListDialog(mContext)
                        .setTitle("List")
                        .setItems("content0", "content1", "content3")
                        .setClickListener { dialog, result, position ->
                            result.toast()
                            dialog.dismiss()
                        }.show()
            }


        }
    }

    private fun countDownStart() {
        countDownStop()
        if (job == null) {
            job = vbCountDownCoroutines(countDown,
                    onStart = {
                        "倒计时开始".log()
                    },
                    onTick = {
                        mDataBinding.tvContent.text = it.toString()
                        "正在倒计时$it".log()

                    },
                    onFinish = {
                        mDataBinding.tvContent.text = "倒计时结束"
                        "倒计时结束".log()

                    },
                    onCancel = {
                        "倒计时取消".log()
                    },
                    scope = lifecycleScope)
        }
    }

    private fun countDownStop() {
        if (job != null) {
            job?.cancel()
            job = null
        }
    }


}

