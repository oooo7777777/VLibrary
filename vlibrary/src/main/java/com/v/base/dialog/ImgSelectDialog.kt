package com.v.base.dialog

import android.view.View
import com.qw.photo.CoCo
import com.qw.photo.callback.CoCoAdapter
import com.qw.photo.callback.CoCoCallBack
import com.qw.photo.pojo.PickResult
import com.qw.photo.pojo.TakeResult
import com.v.base.R
import com.v.base.annotaion.DialogOrientation
import com.v.base.databinding.BaseDialogImgSelectBinding
import com.v.base.model.ImgSelectViewModel
import com.v.base.utils.ext.log
import com.v.base.utils.otherwise
import com.v.base.utils.yes
import java.io.File
import java.util.*

/**
 * author  : ww
 * desc    : 图片选择框
 * time    : 2021-03-16 09:52:45
 */
class ImgSelectDialog : BaseDialogFragment<BaseDialogImgSelectBinding, ImgSelectViewModel>(),
    View.OnClickListener {

    private var ignoreBy = 200L//期望压缩大小,大小和图片呈现质量不能均衡所以压缩后不一定小于此值
    private var isImageCompression = false//是否开启压缩

    override fun useDirection(): DialogOrientation {
        return DialogOrientation.BOTTOM
    }


    private var listener: PhotoSelectDialogListener? = null

    fun setPhotoSelectDialogListener(listener: PhotoSelectDialogListener): ImgSelectDialog {
        this.listener = listener
        return this
    }

    fun setImageCompression(isImageCompression: Boolean): ImgSelectDialog {
        this.isImageCompression = isImageCompression
        return this
    }

    fun setIgnoreBy(ignoreBy: Long): ImgSelectDialog {
        this.ignoreBy = ignoreBy
        return this
    }

    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {
        mViewModel.fileSuccess.observe(this, androidx.lifecycle.Observer {
            listener?.run {
                onSuccess(it)
            }
            dismiss()
        })
    }


    interface PhotoSelectDialogListener {
        fun onSuccess(file: File)
    }

    override fun onClick(v: View) {

        when (v.id) {
            R.id.tvCamera -> {
                CoCo.with(this)
                    .take(mViewModel.createSDCardFile(mContext))
                    .start(object : CoCoCallBack<TakeResult> {
                        override fun onSuccess(data: TakeResult) {
                            var file = data.savedFile!!
                            dispose(file)
                        }

                        override fun onFailed(exception: Exception) {}
                    })
            }
            R.id.tvPhoto -> {
                CoCo.with(this)
                    .pick()
                    .start(object : CoCoAdapter<PickResult>() {
                        override fun onSuccess(data: PickResult) {
                            var file = mViewModel.getFilePathForN(mContext, data.originUri)!!
                            dispose(file)
                        }
                    })
            }
        }
    }

    private fun dispose(file: File) {
        (mViewModel.formatSize(mContext, file.length())).log()
        (isImageCompression).yes {
            mViewModel.imageCompression(this@ImgSelectDialog, file, ignoreBy)
        }.otherwise {
            mViewModel.fileSuccess.value = file
        }

    }

}