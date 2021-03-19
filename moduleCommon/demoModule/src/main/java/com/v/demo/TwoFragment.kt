package com.v.demo

import android.view.View
import com.azhon.appupdate.config.UpdateConfiguration
import com.azhon.appupdate.manager.DownloadManager
import com.v.base.BaseFragment
import com.v.base.dialog.HintDialog
import com.v.base.dialog.ListDialog
import com.v.base.dialog.ImgSelectDialog
import com.v.base.utils.*
import com.v.demo.databinding.DmFragmentTowBinding
import com.v.demo.model.AppViewModel
import com.v.demo.model.DemoViewModel
import java.io.File


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class TwoFragment : BaseFragment<DmFragmentTowBinding, DemoViewModel>(), View.OnClickListener {
    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {

        getApplicationViewModel(mContext.application, AppViewModel::class.java).userBane.observe(
            this,
            androidx.lifecycle.Observer {
                mViewBinding.userBean = it
            })

    }

    override fun onClick(v: View) {
        when (v.id) {
            mViewBinding.btAsync.id -> {
                mViewModel.demoAsync {
                    it.toast()
                }
            }
            mViewBinding.btAppUpdate.id -> {

                val configuration = UpdateConfiguration()
                    .setUsePlatform(false)
                    .setForcedUpgrade(false) //设置强制更新

                val manager: DownloadManager = DownloadManager.getInstance(mContext)
                manager
                    .setConfiguration(configuration)
                    .setApkVersionCode(mContext.getAppVersionCode())
                    .setApkVersionName(mContext.getAppVersionName())
                    .setApkDescription("更新内容")
                    .setApkName("appupdate.apk")
                    .setApkUrl("https://raw.githubusercontent.com/azhon/AppUpdate/master/apk/appupdate.apk")
                    .setSmallIcon(R.mipmap.dm_ic_launcher)
                    .download()
            }
            mViewBinding.bt0.id -> {
                "点击间隔默认500".toast()
            }
            mViewBinding.bt1.id -> {
                "点击间隔1000".toast()
            }
            mViewBinding.bt2.id -> {
                "显示toast".toast()
            }
            mViewBinding.bt3.id -> {
                "打印日志".logE()
            }

            mViewBinding.bt4.id -> {
                HintDialog()
                    .setTitle("提示")
                    .setContent("确定保存吗?")
                    .setButtonText("取消", "确定")
                    .setHintDialogClickListener(object : HintDialog.HintDialogClickListener {
                        override fun onClick(hintDialog: HintDialog, position: Int) {
                            hintDialog.dismiss()

                        }

                    }).show(mContext)
            }
            mViewBinding.bt5.id -> {
                var list = ArrayList<String>()
                for (i in 1..3) {
                    list.add("Content$i")
                }
                ListDialog()
                    .setList(list)
                    .setListDialogListener(object : ListDialog.ListDialogListener {
                        override fun onItem(dialog: ListDialog, result: String, position: Int) {
                            dialog.dismiss()
                            result.toast()
                        }

                    }).show(mContext)
            }
            mViewBinding.bt6.id -> {
                ImgSelectDialog()
                    .setImageCompression(true)
                    .setIgnoreBy(100)
                    .setPhotoSelectDialogListener(object :
                        ImgSelectDialog.PhotoSelectDialogListener {
                        override fun onSuccess(file: File) {

                            mViewBinding.ivIcon.loadCircle(file)
                            mViewBinding.ivIcon.load(file, 10f)

                        }

                    }).show(mContext)


            }
        }
    }
}