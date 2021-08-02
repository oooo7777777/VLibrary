package com.v.demo

import android.view.View
import androidx.lifecycle.Observer
import com.v.base.VBFragment
import com.v.base.dialog.VBHintDialog
import com.v.base.dialog.VBListDialog
import com.v.base.utils.ext.goActivity
import com.v.base.utils.ext.log
import com.v.demo.bean.BannerBean
import com.v.demo.databinding.FragmentThreeBinding
import com.v.demo.model.DataViewModel


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class ThreeFragment : VBFragment<FragmentThreeBinding, DataViewModel>(), View.OnClickListener {

    override fun initData() {
        mDataBinding.v = this
    }

    override fun createObserver() {

        mViewModel.content.observe(this, Observer {
            mDataBinding.content = it
        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            mDataBinding.bt0.id -> {
                mViewModel.setContent("ViewModel 对象存在的时间范围是获取 ViewModel 时传递给 ViewModelProvider 的 Lifecycle。ViewModel 将一直留在内存中，直到限定其存在时间范围的 Lifecycle 永久消失：对于 Activity，是在 Activity 完成时；而对于 Fragment，是在 Fragment 分离时。")
            }

            mDataBinding.bt1.id -> {
                goActivity(EventBusDemoActivity::class.java)
            }

            mDataBinding.bt2.id -> {
                goActivity(NetworkActivity::class.java)
            }

            mDataBinding.bt3.id -> {
                VBHintDialog()
                    .setTitle("提示")
                    .setContent("确定保存吗?")
                    .setButtonText("取消", "确定")
                    .setClickListener { hintDialog, position ->
                        hintDialog.dismiss()
                        mViewModel.content.value = "点击$position"
                    }.show(mContext)
            }
            mDataBinding.bt4.id -> {

                VBListDialog()
                    .setItems("content0","content1","content3")
                    .setClickListener { dialog, result, position ->
                        dialog.dismiss()
                        mViewModel.content.value = result
                    }.show(mContext)
            }

            mDataBinding.bt5.id -> {
                goActivity(TestActivity::class.java)
            }

        }
    }

}

