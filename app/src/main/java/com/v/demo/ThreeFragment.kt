package com.v.demo

import android.view.View
import com.v.base.VBBlankViewModel
import com.v.base.VBFragment
import com.v.base.dialog.VBHintDialog
import com.v.base.dialog.VBListDialog
import com.v.base.utils.ext.goActivity
import com.v.base.utils.toast
import com.v.demo.databinding.FragmentThreeBinding


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class ThreeFragment : VBFragment<FragmentThreeBinding, VBBlankViewModel>(), View.OnClickListener {


    override fun initData() {
        mDataBinding.v = this
    }

    override fun createObserver() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            mDataBinding.bt2.id -> {
                goActivity(NetworkActivity::class.java)
            }

            mDataBinding.bt3.id -> {
                VBHintDialog(mContext)
                    .setTitle("提示")
                    .setContent("确定保存吗?")
                    .setButtonText("取消", "确定")
                    .setClickListener { hintDialog, position ->
                        hintDialog.dismiss()
                    }.show()
            }
            mDataBinding.bt4.id -> {

                VBListDialog(mContext)
                    .setItems("content0", "content1", "content3")
                    .setClickListener { dialog, result, position ->
                        result.toast()
                        dialog.dismiss()
                    }.show()
            }


        }
    }

}

