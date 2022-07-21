package com.v.base.dialog

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.v.base.R
import com.v.base.databinding.VbLayoutLoadingBinding

class VBLoadingDialog(mContext: Context) : VBDialog<VbLayoutLoadingBinding>(mContext) {


    override fun initData() {
    }


    fun setMsg(msg: String, color: String = "#ffffff") {
        if (!msg.isNullOrEmpty()) {
            mDataBinding.tvMsg.text = msg
            mDataBinding.tvMsg.visibility = View.VISIBLE
            mDataBinding.tvMsg.setTextColor(Color.parseColor(color))
        }
    }

    fun setCanceled(isCancelable: Boolean): VBLoadingDialog {
        setDialogCancelable(isCancelable)
        return this
    }

    override fun useAnimations(): Boolean {
        return false
    }

}