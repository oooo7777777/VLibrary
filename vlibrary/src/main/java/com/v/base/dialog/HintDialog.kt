package com.v.base.dialog

import android.graphics.Color
import android.view.Gravity
import android.view.View
import com.v.base.BlankViewModel
import com.v.base.databinding.BaseDialogHintBinding
import com.v.base.utils.ext.onClickAnimator
import com.v.base.utils.ext.otherwise
import com.v.base.utils.ext.yes


/**
 * author  : ww
 * desc    : 提示框
 * time    : 2021-03-16 09:52:45
 */
class HintDialog : BaseDialogFragment<BaseDialogHintBinding, BlankViewModel>() {

    private var title: String = ""
    private var content: String = ""
    private var contentGravity: Int = Gravity.CENTER
    private var btTexts = ArrayList<String>()
    private var btTextColors = ArrayList<String>()

    private var listener: HintDialogClickListener? = null


    override fun initData() {


        mViewBinding?.run {

            tvLeft.onClickAnimator {
                (listener == null).yes {
                    dismiss()
                }.otherwise {
                    listener!!.onClick(this@HintDialog, 0)
                }
            }

            tvRight.onClickAnimator {
                (listener == null).yes {
                    dismiss()
                }.otherwise {
                    listener!!.onClick(this@HintDialog, 1)
                }
            }

            title.isNotEmpty().run {
                tvTitle.text = title
                tvTitle.visibility = View.VISIBLE
            }

            tvContent.text = content
            tvContent.gravity = contentGravity


            if (btTexts.size == 1) {
                tvRight.text = btTexts[0]
            } else if (btTexts.size == 2) {
                baseViewWire.visibility = View.VISIBLE
                tvLeft.visibility = View.VISIBLE

                tvLeft.text = btTexts[0]
                tvRight.text = btTexts[1]
            }


            if (btTextColors.size == 1) {
                tvLeft.setTextColor(Color.parseColor(btTextColors[0]))
            } else if (btTextColors.size == 2) {
                tvLeft.setTextColor(Color.parseColor(btTextColors[0]))
                tvRight.setTextColor(Color.parseColor(btTextColors[1]))
            }
        }

    }

    override fun createObserver() {
    }


    fun setTitle(text: String): HintDialog {
        this.title = text
        return this
    }

    fun setContent(content: String, contentGravity: Int = Gravity.CENTER): HintDialog {
        this.content = content
        this.contentGravity = contentGravity
        return this
    }

    fun setHintDialogClickListener(listener: HintDialogClickListener): HintDialog {
        this.listener = listener
        return this
    }


    /**
     * 设置按钮文字内容
     * @param btTexts size 1, left
     * @param btTexts size 2, left right
     */
    fun setButtonText(vararg btTexts: String): HintDialog {
       (btTexts.isEmpty() || btTexts.size > 2).yes {
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
    fun setButtonTextColor(vararg btTextColors: String): HintDialog {
         (btTextColors.isEmpty() || btTextColors.size > 2).yes {
            throw IllegalStateException(" range of param btnTexts length is [1,2]!")
        }
        btTextColors.forEach {
            this.btTextColors.add(it)
        }

        return this
    }


    interface HintDialogClickListener {
        fun onClick(hintDialog: HintDialog, position: Int)
    }


}