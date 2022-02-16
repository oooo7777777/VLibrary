package com.v.base.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import com.v.base.R
import com.v.base.VBApplication

class VBLoadingDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window!!.setBackgroundDrawable(ColorDrawable(0))
        setContentView(R.layout.vb_layout_loading)
    }

    /**
     * 设置是否返回按钮取消
     */
    fun setDialogCancelable(isCancelable: Boolean): VBLoadingDialog {
        setCanceledOnTouchOutside(isCancelable)
        setCancelable(isCancelable)
        return this
    }

}