package com.v.base.dialog

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import com.v.base.R
import com.v.base.VBConfig
import com.v.base.annotaion.VBDialogOrientation
import com.v.base.utils.vbGetAllChildViews
import com.v.log.util.logI
import java.lang.reflect.ParameterizedType


/**
 * author  : ww
 * desc    :
 * time    : 2021-03-16 09:52:45
 */
abstract class VBDialog<VB : ViewDataBinding>(private val mContext: Context) :
    Dialog(mContext, R.style.MyDialog) {

    private var onDismiss: (() -> Unit)? = null
    private var onShow: (() -> Unit)? = null

    fun setListener(onShow: (() -> Unit), onDismiss: (() -> Unit)) {
        this.onShow = onShow
        this.onDismiss = onDismiss
    }


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
        javaClass.name.logI()
        if (mContext is AppCompatActivity) {
            lifecycleOwner(mContext)
        }
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
     * 将对话框附加到生命周期并在生命周期被销毁时将其关闭。
     */
    private fun lifecycleOwner(owner: LifecycleOwner? = null): VBDialog<*> {
        val observer = DialogLifecycleObserver(::dismiss)

//        val lifecycleOwner = owner ?: (mContext as? LifecycleOwner
//            ?: throw IllegalStateException(
//                "$mContext is not a LifecycleOwner."
//            ))
//        lifecycleOwner.lifecycle.addObserver(observer)

        //如果当前的$mContext 不是LifecycleOwner 则不会添加
        val lifecycleOwner = owner ?: mContext as? LifecycleOwner
        lifecycleOwner?.lifecycle?.addObserver(observer)
        return this
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
     * 变暗系数 系数0.0-1.0 系数越高暗度越高
     */
    open fun useDimAmount(): Float {
        return VBConfig.options.dialogDimAmount
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

    /**
     * dialog是否全屏,会隐藏掉状态栏
     */
    open fun useFullScreen(): Boolean {
        return false
    }

    private fun setStyle() {
        window?.run {
            //设置全屏属性
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            requestFeature(Window.FEATURE_NO_TITLE)

            if (useFullScreen()) {
                setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }

            setBackgroundDrawableResource(android.R.color.transparent)
            decorView.setPadding(0, 0, 0, 0)
            val wlp = attributes

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

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                wlp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
            attributes = wlp
        }

    }

    override fun dismiss() {
        super.dismiss()
        onDismiss?.invoke()
    }

    override fun show() {
        super.show()
        onShow?.invoke()
    }

}