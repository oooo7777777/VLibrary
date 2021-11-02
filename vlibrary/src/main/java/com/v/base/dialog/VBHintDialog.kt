package com.v.base.dialog

import android.graphics.Color
import android.view.Gravity
import android.view.View
import com.v.base.VBBlankViewModel
import com.v.base.databinding.VbDialogHintBinding


/**
 * author  : ww
 * desc    : 提示框
 * time    : 2021-03-16 09:52:45
 */
class VBHintDialog : VBDialogFragment<VbDialogHintBinding, VBBlankViewModel>(),
    View.OnClickListener {

    private var title: String = ""
    private var content: String = ""
    private var contentGravity: Int = Gravity.CENTER
    private var btTexts = ArrayList<String>()
    private var btTextColors = ArrayList<String>()

    private var listener: ((hintDialog: VBHintDialog, position: Int) -> Unit)? = null


    override fun initData() {

        mDataBinding.v = this
        if (!title.isNullOrEmpty()) {
            mDataBinding.tvTitle.text = title
            mDataBinding.tvTitle.visibility = View.VISIBLE
        }

        mDataBinding.tvContent.text = content
        mDataBinding.tvContent.gravity = contentGravity

        if (btTexts.size == 1) {
            mDataBinding.tvRight.text = btTexts[0]
        } else if (btTexts.size == 2) {
            mDataBinding.baseViewWire.visibility = View.VISIBLE
            mDataBinding.tvLeft.visibility = View.VISIBLE

            mDataBinding.tvLeft.text = btTexts[0]
            mDataBinding.tvRight.text = btTexts[1]
        }

        if (btTextColors.size == 1) {
            mDataBinding.tvLeft.setTextColor(Color.parseColor(btTextColors[0]))
        } else if (btTextColors.size == 2) {
            mDataBinding.tvLeft.setTextColor(Color.parseColor(btTextColors[0]))
            mDataBinding.tvRight.setTextColor(Color.parseColor(btTextColors[1]))
        }

    }

    override fun createObserver() {
    }


    fun setTitle(text: String): VBHintDialog {
        this.title = text
        return this
    }

    fun setContent(content: String, contentGravity: Int = Gravity.CENTER): VBHintDialog {
        this.content = content
        this.contentGravity = contentGravity
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
            throw IllegalStateException(" range of param btnTexts length is [1,2]!")
        }
        btTexts.forEach {
            this.btTexts.add(it)
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
            throw IllegalStateException(" range of param btnTexts length is [1,2]!")
        }
        btTextColors.forEach {
            this.btTextColors.add(it)
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