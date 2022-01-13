package com.v.base.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import com.v.base.R
import com.v.base.annotaion.VBDialogOrientation
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
        method.invoke(null, layoutInflater) as VB
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        javaClass.name.logI()
        setStyle()
        setContentView(mDataBinding.root)
        initData()
        setDialogCancelable(useCancellation())

    }

    protected abstract fun initData()

    /**
     * 设置是否返回按钮取消
     */
     fun setDialogCancelable(isCancelable: Boolean) {
        setCanceledOnTouchOutside(isCancelable)
        setCancelable(isCancelable)
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
     * 设置返回键不退出dialog
     */
    open fun useCancellation(): Boolean {
        return true
    }

    private fun setStyle() {

        val window: Window = window!!

        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.decorView.setPadding(0, 0, 0, 0)
        val wlp = window.attributes
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT

        if (useDim()) {
            wlp.dimAmount = 0.3f
        } else {
            wlp.dimAmount = 0f
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }


        when (useDirection()) {
            VBDialogOrientation.TOP -> {
                wlp.gravity = Gravity.TOP
                window.setWindowAnimations(R.style.vb_top_dialog_anim)
            }
            VBDialogOrientation.BOTTOM -> {
                wlp.gravity = Gravity.BOTTOM
                window.setWindowAnimations(R.style.vb_bottom_dialog_anim)
            }
            VBDialogOrientation.LEFT -> {
                wlp.gravity = Gravity.CENTER
                window.setWindowAnimations(R.style.vb_left_dialog_anim)
            }

            VBDialogOrientation.RIGHT -> {
                wlp.gravity = Gravity.CENTER
                window.setWindowAnimations(R.style.vb_right_dialog_anim)
            }

            VBDialogOrientation.CENTRE -> {
                wlp.gravity = Gravity.CENTER
                window.setWindowAnimations(R.style.vb_dialog_anim)
            }
        }

        window.attributes = wlp
    }


}