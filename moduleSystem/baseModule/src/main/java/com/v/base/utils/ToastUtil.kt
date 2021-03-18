package com.v.base.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import com.v.base.R


object ToastUtil {
    private var isJumpWhenMore = true
    private var mToast: Toast? = null
    private var mText: TextView? = null
    fun showToast(context: Context, content: String) {
        if (isJumpWhenMore) cancelToast()
        if (mToast == null) {
            mToast = Toast(context)
            mToast?.let {
                it.setGravity(Gravity.CENTER, 0, 0)
                it.duration = Toast.LENGTH_SHORT
                val layout =
                    LayoutInflater.from(context)
                        .inflate(R.layout.base_layout_toast, null)
                mText = layout.findViewById(R.id.tvContent)
                it.view = layout
            }

        }


        mText!!.text = content
        mToast!!.duration = Toast.LENGTH_LONG
        mToast!!.show()
    }


    private fun cancelToast() {
        if (mToast != null) {
            mToast!!.cancel()
            mToast = null
            mText = null
        }
    }

}