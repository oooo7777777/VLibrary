package com.v.base.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.databinding.ViewDataBinding
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.v.base.R
import com.v.base.VBConfig
import com.v.base.annotaion.VBDialogOrientation
import com.v.base.utils.isWhiteColor
import com.v.base.utils.log
import com.v.base.utils.logI
import com.v.base.utils.vbGetAllChildViews
import java.lang.reflect.ParameterizedType
import android.view.WindowManager

import android.os.Build
import androidx.appcompat.app.AlertDialog
import android.graphics.drawable.GradientDrawable


/**
 * author  : ww
 * desc    :
 * time    : 2021-03-16 09:52:45
 */
abstract class VBDialog<VB : ViewDataBinding>(private val mContext: Context) :
    Dialog(mContext, R.style.MyDialog) {

    private var isDialogCancelable = true

    protected val mDataBinding: VB by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.isAccessible = true
        method.invoke(null, layoutInflater) as VB
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        javaClass.name.log()
        setStyle()
        setContentView(mDataBinding.root)
        setCanceled()
        initData()
    }


    protected abstract fun initData()


    private fun setCanceled() {
        setCanceledOnTouchOutside(isDialogCancelable)
        setCancelable(isDialogCancelable)
        if (isDialogCancelable) {
            mDataBinding.root.setOnClickListener {
                dismiss()
            }
            (mDataBinding.root as ViewGroup).vbGetAllChildViews().forEach {
                it.isClickable = true
            }
        } else {
            mDataBinding.root.setOnClickListener {

            }
            (mDataBinding.root as ViewGroup).vbGetAllChildViews().forEach {
                it.isClickable = false
            }
        }
    }

    fun setDialogCancelable(isCancelable: Boolean) {
        this.isDialogCancelable = isCancelable
        setCanceled()
    }


    /**
     * 状态栏字体颜色 true为深色 false为亮色
     */
    open fun useStatusBarBright(): Boolean =
        isWhiteColor(Color.parseColor(VBConfig.options.statusBarColor))

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
     * 变暗系数 系数0.0-1.0 系数越高暗度越高
     */
    open fun useDimAmount(): Float {
        return 0.5f
    }

    /**
     * 是否开启弹出动画
     */
    open fun useAnimations(): Boolean {
        return true
    }

    /**
     * 自定义弹出动画
     */
    open fun useAnimationsRes(): Int {
        return 0
    }


    private fun setStyle() {
        window?.run {
            requestFeature(Window.FEATURE_NO_TITLE)
//            setBackgroundDrawableResource(android.R.color.transparent)
//            decorView.setPadding(0, 0, 0, 0)

            val gradientDrawable = GradientDrawable()
            gradientDrawable.setColor(0x00000000)
            window!!.setBackgroundDrawable(gradientDrawable) //设置对话框边框背景,必须在代码中设置对话框背景，不然对话框背景是黑色的

            val wlp = attributes
//            wlp.width = useWidth()
//            wlp.height = useHeight()

            if (useDim()) {
                wlp.dimAmount = useDimAmount()
            } else {
                wlp.dimAmount = 0f
                clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }


            when (useDirection()) {
                VBDialogOrientation.TOP -> {
                    wlp.gravity = Gravity.TOP
                    if (useAnimations() && useAnimationsRes() == 0)
                        setWindowAnimations(R.style.vb_top_dialog_anim)
                }
                VBDialogOrientation.BOTTOM -> {
                    wlp.gravity = Gravity.BOTTOM
                    if (useAnimations() && useAnimationsRes() == 0)
                        setWindowAnimations(R.style.vb_bottom_dialog_anim)
                }
                VBDialogOrientation.LEFT -> {
                    wlp.gravity = Gravity.CENTER
                    if (useAnimations() && useAnimationsRes() == 0)
                        setWindowAnimations(R.style.vb_left_dialog_anim)
                }

                VBDialogOrientation.RIGHT -> {
                    wlp.gravity = Gravity.CENTER
                    if (useAnimations() && useAnimationsRes() == 0)
                        setWindowAnimations(R.style.vb_right_dialog_anim)
                }

                VBDialogOrientation.CENTRE -> {
                    wlp.gravity = Gravity.CENTER
                    if (useAnimations() && useAnimationsRes() == 0)
                        setWindowAnimations(R.style.vb_dialog_anim)
                }
            }
            if (useAnimations() && useAnimationsRes() != 0) {
                setWindowAnimations(useAnimationsRes())
            }

            attributes = wlp
        }

    }

    override fun dismiss() {
        super.dismiss()
        if (mContext is Activity) {
            ImmersionBar.destroy(mContext, this)
        }

    }

    override fun show() {
        super.show()
        if (mContext is Activity) {
            //状态栏字体颜色 true为深色 false为亮色
            //状态栏颜色趋近于白色时，会智能将状态栏字体颜色变换为黑色
            ImmersionBar.with(mContext, this)
                .statusBarDarkFont(useStatusBarBright())
                .navigationBarDarkIcon(useStatusBarBright())
                .init()
        }
    }
}