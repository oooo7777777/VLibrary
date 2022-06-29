package com.v.base


import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.v.base.databinding.VbTitleBarBinding
import com.v.base.utils.*

/**
 * @Author : ww
 * desc    :
 * time    : 2021/11/2
 */
class VBTitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {


    var mDataBinding: VbTitleBarBinding =
        DataBindingUtil.inflate(LayoutInflater.from(context),
            R.layout.vb_title_bar,
            this,
            true)

    init {
        mDataBinding.ivLeft.setImageResource(VBConfig.options.toolbarBackRes)
        setToolbarColor(VBConfig.options.toolbarColor)
    }


    fun useToolbar(show: Boolean) {
        mDataBinding.toolbar.visibility = if (show) View.VISIBLE else View.GONE
        mDataBinding.ivLine.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun useLine(show: Boolean) {
        mDataBinding.ivLine.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setToolbarColor(color: Int) {
        mDataBinding.toolbar.setBackgroundColor(color)
    }

    fun setToolbarTransparent() {
        mDataBinding.toolbar.setBackgroundColor(Color.TRANSPARENT)
        mDataBinding.ivStatusBar.setBackgroundColor(Color.TRANSPARENT)
        mDataBinding.ivStatusBar.vbSetViewLayoutParams(h = context.vbGetStatusBarHeight())
    }

    fun setTitle(
        title: String = "",
        titleColor: Int = VBConfig.options.toolbarTitleColor,
        isShowBottomLine: Boolean = true,
    ) {
        mDataBinding.tvTitle.text = title
        mDataBinding.tvTitle.setTextColor(titleColor)
        mDataBinding.tvTitle.vbTextBold(true)
        mDataBinding.ivLine.visibility = if (isShowBottomLine) View.VISIBLE else View.GONE
    }


    fun setLeft(
        title: String = "",
        titleColor: Int = Color.BLACK,
        listener: OnClickListener? = null,
    ) {
        mDataBinding.ivLeft.visibility = View.GONE
        mDataBinding.tvLeft.visibility = View.VISIBLE

        mDataBinding.tvLeft.text = title
        mDataBinding.tvLeft.setTextColor(titleColor)

        if (listener != null) {
            mDataBinding.flLeft.vbOnClickListener {
                listener.onClick(it)
            }
        }
    }

    fun setLeft(
        res: Int = VBConfig.options.toolbarBackRes,
        listener: OnClickListener? = null,
    ) {

        mDataBinding.ivLeft.visibility = View.VISIBLE
        mDataBinding.tvLeft.visibility = View.GONE

        mDataBinding.ivLeft.setImageResource(res)

        if (listener != null) {
            mDataBinding.flLeft.vbOnClickListener {
                listener.onClick(it)
            }
        }
    }


    fun setRight(
        title: String = "",
        titleColor: Int = Color.BLACK,
        listener: OnClickListener? = null,
    ) {
        mDataBinding.ivRight.visibility = View.GONE
        mDataBinding.tvRight.visibility = View.VISIBLE

        mDataBinding.tvRight.text = title
        mDataBinding.tvRight.setTextColor(titleColor)

        if (listener != null) {
            mDataBinding.flRight.vbOnClickListener {
                listener.onClick(it)
            }
        }

    }

    fun setRight(
        res: Int = R.mipmap.vb_ic_back_black,
        listener: OnClickListener? = null,
    ) {

        mDataBinding.ivRight.visibility = View.VISIBLE
        mDataBinding.tvRight.visibility = View.GONE

        mDataBinding.ivRight.setImageResource(res)

        if (listener != null) {
            mDataBinding.flRight.vbOnClickListener {
                listener.onClick(it)
            }
        }
    }


}
