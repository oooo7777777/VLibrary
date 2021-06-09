package com.v.demo

import android.view.View
import androidx.lifecycle.Observer
import com.v.base.BaseFragment
import com.v.base.dialog.HintDialog
import com.v.base.dialog.ImgSelectDialog
import com.v.base.dialog.ListDialog
import com.v.base.utils.ext.goActivity
import com.v.demo.databinding.FragmentThreeBinding
import com.v.demo.model.DataViewModel
import java.io.File


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class ThreeFragment : BaseFragment<FragmentThreeBinding, DataViewModel>(), View.OnClickListener {

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
                mContext.goActivity(EventBusDemoActivity::class.java)
            }

            mDataBinding.bt2.id -> {
                mContext.goActivity(NetworkActivity::class.java)
            }

            mDataBinding.bt3.id -> {
                HintDialog()
                    .setTitle("提示")
                    .setContent("确定保存吗?")
                    .setButtonText("取消", "确定")
                    .setHintDialogClickListener(object : HintDialog.HintDialogClickListener {
                        override fun onClick(hintDialog: HintDialog, position: Int) {
                            hintDialog.dismiss()

                            mViewModel.content.value = "点击$position"
                        }

                    }).show(mContext)
            }
            mDataBinding.bt4.id -> {
                var list = ArrayList<String>()
                for (i in 1..3) {
                    list.add("Content$i")
                }
                ListDialog()
                    .setList(list)
                    .setListDialogListener(object : ListDialog.ListDialogListener {
                        override fun onItem(dialog: ListDialog, result: String, position: Int) {
                            dialog.dismiss()

                            mViewModel.content.value = result
                        }

                    }).show(mContext)
            }
            mDataBinding.bt5.id -> {
                ImgSelectDialog()
                    .setImageCompression(true)
                    .setIgnoreBy(100)
                    .setPhotoSelectDialogListener(object :
                        ImgSelectDialog.PhotoSelectDialogListener {
                        override fun onSuccess(file: File) {
                            mViewModel.content.value = file.toString()
                        }

                    }).show(mContext)


            }
        }
    }

}