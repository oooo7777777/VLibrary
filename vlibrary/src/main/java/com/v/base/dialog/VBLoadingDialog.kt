package com.v.base.dialog

import android.graphics.Color
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.v.base.databinding.VbLayoutLoadingBinding

class VBLoadingDialog(mContext: AppCompatActivity) : VBDialog<VbLayoutLoadingBinding>(mContext) {


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