package com.v.base.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.ViewDataBinding
import com.v.base.R
import com.v.base.annotaion.VBDialogOrientation
import com.v.base.utils.ext.log
import com.v.base.utils.ext.logI
import java.lang.reflect.ParameterizedType

/**
 * author  : ww
 * desc    :
 * time    : 2021-03-16 09:52:45
 */
abstract class VBDialog<VB : ViewDataBinding>(mContext: Context) : Dialog(mContext) {


    protected val mDataBinding: VB by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.isAccessible = true
        method.invoke(null, layoutInflater) as VB
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        javaClass.name.logI()
        setStyle()
        setContentView(mDataBinding.root)
        initData()

    }

    protected abstract fun initData()

    /**
     * 设置是否返回按钮取消
     */
    fun setDialogCancelable(isCancelable: Boolean) {
        setCanceledOnTouchOutside(isCancelable)
        setCancelable(isCancelable)
        "isCancelable:" + isCancelable.log()
    }

    /**
     * 设置dialog弹出方向 [VBDialogOrientation]
     */
    open fun useDirection(): VBDialogOrientation = VBDialogOrientation.CENTRE

    /**
     * 是否变暗
     */
    open fun useDim(): Boolean {
        return true
    }

    /**
     * 是否开启弹出动画
     */
    open fun useAnimations(): Boolean {
        return true
    }

    /**
     * 设置宽
     */
    open fun useWidth(): Int {
        return ViewGroup.LayoutParams.MATCH_PARENT
    }

    /**
     * 设置高
     */
    open fun useHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    private fun setStyle() {

        val window: Window = window!!

        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.decorView.setPadding(0, 0, 0, 0)
        val wlp = window.attributes
        wlp.width = useWidth()
        wlp.height = useHeight()

        if (useDim()) {
            wlp.dimAmount = 0.7f
        } else {
            wlp.dimAmount = 0f
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }


        when (useDirection()) {
            VBDialogOrientation.TOP -> {
                wlp.gravity = Gravity.TOP
                if (useAnimations())
                    window.setWindowAnimations(R.style.vb_top_dialog_anim)
            }
            VBDialogOrientation.BOTTOM -> {
                wlp.gravity = Gravity.BOTTOM
                if (useAnimations())
                    window.setWindowAnimations(R.style.vb_bottom_dialog_anim)
            }
            VBDialogOrientation.LEFT -> {
                wlp.gravity = Gravity.CENTER
                window.setWindowAnimations(R.style.vb_left_dialog_anim)
            }

            VBDialogOrientation.RIGHT -> {
                wlp.gravity = Gravity.CENTER
                if (useAnimations())
                    window.setWindowAnimations(R.style.vb_right_dialog_anim)
            }

            VBDialogOrientation.CENTRE -> {
                wlp.gravity = Gravity.CENTER
                if (useAnimations())
                    window.setWindowAnimations(R.style.vb_dialog_anim)
            }
        }

        window.attributes = wlp
    }


}