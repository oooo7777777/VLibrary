package com.v.demo

import android.view.View
import com.azhon.appupdate.config.UpdateConfiguration
import com.azhon.appupdate.manager.DownloadManager
import com.v.base.BaseFragment
import com.v.base.BlankViewModel
import com.v.base.dialog.HintDialog
import com.v.base.dialog.ListDialog
import com.v.base.dialog.PhotoSelectDialog
import com.v.base.utils.ext.*
import com.v.demo.databinding.DmFragmentTowBinding
import com.v.demo.model.AppViewModel
import java.io.File


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class TwoFragment : BaseFragment<DmFragmentTowBinding, BlankViewModel>(), View.OnClickListener {
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
            R.id.btAppUpdate -> {

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
            R.id.bt0 -> {
                "点击间隔默认500".log()
            }
            R.id.bt1 -> {
                "点击间隔1000".log()
            }
            R.id.bt2 -> {
                "显示toast".toast()
            }

            R.id.bt3 -> {
                "打印日志".logE()
            }

            R.id.bt4 -> {
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
            R.id.bt5 -> {
                var list = ArrayList<String>()
                for (i in 0 until 3) {
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
            R.id.bt6 -> {
                PhotoSelectDialog()
                    .setPhotoSelectDialogListener(object :
                        PhotoSelectDialog.PhotoSelectDialogListener {
                        override fun onSuccess(file: File) {
                            mViewBinding.ivIcon.loadCircle(file)
                        }

                    }).show(mContext)


            }
        }
    }
}