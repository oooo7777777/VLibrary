package com.v.base.dialog

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.v.base.BaseViewModel
import com.v.base.EventLiveData
import com.v.base.LoadingDialog
import com.v.base.R
import com.v.base.annotaion.DialogOrientation
import com.v.base.utils.logI
import java.lang.reflect.ParameterizedType

/**
 * author  : ww
 * desc    :
 * time    : 2021-03-16 09:52:45
 */
abstract class BaseDialogFragment<VB : ViewDataBinding, VM : BaseViewModel> : DialogFragment() {
    lateinit var mContext: Context

    protected lateinit var mViewBinding: VB

    private val loadDialog by lazy {
        LoadingDialog(mContext).setDialogCancelable(true)
    }

    protected val mViewModel: VM by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this).get(aClass)//绑定当前的DialogFragment
//        ViewModelProvider(requireActivity()).get(aClass)//绑定当前的打开DialogFragment的载体
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            this.mContext = context
        }
        javaClass.name.logI()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        mViewBinding = method.invoke(null, layoutInflater, container, false) as VB
        setStyle()
        registerUiChange()

        return mViewBinding.root
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        createObserver()
    }

    protected abstract fun initData()

    protected abstract fun createObserver()

    /**
     * 设置dialog弹出方向 [DialogOrientation]
     */
    open fun useDirection(): DialogOrientation = DialogOrientation.CENTRE

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

        val window: Window = dialog!!.window!!

        dialog!!.setCanceledOnTouchOutside(useCancellation())
        isCancelable = useCancellation()

        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.decorView.setPadding(0, 0, 0, 0)
        val wlp = window.attributes
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT

        if (useDim()) {
            wlp.dimAmount = 0.5f
        } else {
            wlp.dimAmount = 0f
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }


        when (useDirection()) {
            DialogOrientation.TOP -> {
                wlp.gravity = Gravity.TOP
                window.setWindowAnimations(R.style.base_top_dialog_anim)
            }
            DialogOrientation.BOTTOM -> {
                wlp.gravity = Gravity.BOTTOM
                window.setWindowAnimations(R.style.base_bottom_dialog_anim)
            }
            DialogOrientation.LEFT -> {
                wlp.gravity = Gravity.CENTER
                window.setWindowAnimations(R.style.base_left_dialog_anim)
            }

            DialogOrientation.RIGHT -> {
                wlp.gravity = Gravity.CENTER
                window.setWindowAnimations(R.style.base_right_dialog_anim)
            }

            DialogOrientation.CENTRE -> {
                wlp.gravity = Gravity.CENTER
                window.setWindowAnimations(R.style.base_dialog_anim)
            }
        }

        window.attributes = wlp
    }


    fun show(context: Context) {
        if (context is AppCompatActivity) {
            super.show(context.supportFragmentManager, context.componentName.toString())
        } else if (context is Fragment) {
            super.show(context.childFragmentManager, javaClass.name)
        }
    }

    fun noCancellation(): BaseDialogFragment<*, *> {
        isCancelable = false
        dialog!!.setCanceledOnTouchOutside(useCancellation())
        return this
    }

    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.loadingChange.showDialog.observe(this, Observer {
            if (!loadDialog.isShowing) {
                loadDialog.show()
            }

        })
        //关闭弹窗
        mViewModel.loadingChange.dismissDialog.observe(this, Observer {

            if (loadDialog.isShowing) {
                loadDialog.dismiss()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        this.viewModelStore.clear()

    }
}