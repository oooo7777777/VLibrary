package com.v.base.dialog

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import androidx.databinding.ViewDataBinding
import com.v.base.VBBlankViewModel
import com.v.base.annotaion.VBDialogOrientation
import com.v.base.databinding.VbDialogHintBinding


/**
 * author  : ww
 * desc    : 提示框
 * time    : 2021-03-16 09:52:45
 */
class VBHintDialog(mContext: Context) : VBDialog<VbDialogHintBinding>(mContext),
    View.OnClickListener {

    private var listener: ((hintDialog: VBHintDialog, position: Int) -> Unit)? = null

    override fun initData() {
        mDataBinding.v = this
    }

    fun getDataBinding(): VbDialogHintBinding {
        return mDataBinding
    }

    fun setTitle(text: String): VBHintDialog {
        mDataBinding.tvTitle.text = text
        mDataBinding.tvTitle.visibility = View.VISIBLE
        return this
    }

    fun setContent(content: String, contentGravity: Int = Gravity.CENTER): VBHintDialog {
        mDataBinding.tvContent.text = content
        mDataBinding.tvContent.gravity = contentGravity
        return this
    }

    fun setClickListener(listener: ((hintDialog: VBHintDialog, position: Int) -> Unit)): VBHintDialog {
        this.listener = listener
        return this
    }

    /**
     * 设置按钮文字内容
     * @param btTexts size 1, left
     * @param btTexts size 2, left right
     */
    fun setButtonText(vararg btTexts: String): VBHintDialog {
        if (btTexts.isEmpty() || btTexts.size > 2) {
            throw IllegalStateException("range of param btnTexts length is [1,2]!")
        }
        if (btTexts.size == 1) {
            mDataBinding.tvRight.text = btTexts[0]
        } else if (btTexts.size == 2) {
            mDataBinding.baseViewWire.visibility = View.VISIBLE
            mDataBinding.tvLeft.visibility = View.VISIBLE

            mDataBinding.tvLeft.text = btTexts[0]
            mDataBinding.tvRight.text = btTexts[1]
        }

        return this
    }

    /**
     * 设置按钮文字颜色
     * @param btTextColors size 1, left
     * @param btTextColors size 2, left right
     */
    fun setButtonTextColor(vararg btTextColors: String): VBHintDialog {
        if (btTextColors.isEmpty() || btTextColors.size > 2) {
            throw IllegalStateException("range of param btnTexts length is [1,2]!")
        }

        if (btTextColors.size == 1) {
            mDataBinding.tvLeft.setTextColor(Color.parseColor(btTextColors[0]))
        } else if (btTextColors.size == 2) {
            mDataBinding.tvLeft.setTextColor(Color.parseColor(btTextColors[0]))
            mDataBinding.tvRight.setTextColor(Color.parseColor(btTextColors[1]))
        }
        return this
    }


    override fun onClick(v: View) {
        when (v.id) {
            mDataBinding.tvLeft.id -> {
                if (listener == null) {
                    dismiss()
                } else {
                    listener!!.invoke(this@VBHintDialog, 0)
                }
            }
            mDataBinding.tvRight.id -> {
                if (listener == null) {
                    dismiss()
                } else {
                    listener!!.invoke(this@VBHintDialog, 1)
                }
            }
        }
    }


}