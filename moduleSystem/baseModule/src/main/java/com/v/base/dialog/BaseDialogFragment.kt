package com.v.base.dialog

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.v.base.BaseViewModel
import com.v.base.R
import com.v.base.utils.ext.logI
import java.lang.reflect.ParameterizedType

abstract class BaseDialogFragment<VB : ViewDataBinding, VM : BaseViewModel> : DialogFragment() {
    lateinit var mContext: Context

    protected lateinit var mViewBinding: VB


    protected val mViewModel: VM by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[1] as Class<VM>
        ViewModelProvider(requireActivity()).get(aClass)
    }

    companion object {
        const val DIRECTION_TOP: Int = 0
        const val DIRECTION_BOTTOM: Int = 1
        const val DIRECTION_LEFT: Int = 2
        const val DIRECTION_RIGHT: Int = 3
        const val DIRECTION_CENTRE: Int = 4
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

    open fun useDirection(): Int = DIRECTION_CENTRE

    open fun useY(): Int = 0

    open fun useX(): Int = 0

    open fun useGravity(): Int = -1

    open fun useDim(): Boolean {
        return true
    }

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
        wlp.y = useY()
        wlp.x = useX()
        if (useDim()) {
            wlp.dimAmount = 0.5f
        } else {
            wlp.dimAmount = 0f
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }


        when (useDirection()) {
            DIRECTION_TOP -> {
                wlp.gravity = if (useGravity() == -1) Gravity.TOP else useGravity()
                window.setWindowAnimations(R.style.base_top_dialog_anim)
            }
            DIRECTION_BOTTOM -> {
                wlp.gravity = if (useGravity() == -1) Gravity.BOTTOM else useGravity()
                window.setWindowAnimations(R.style.base_bottom_dialog_anim)
            }
            DIRECTION_LEFT -> {
                wlp.gravity = if (useGravity() == -1) Gravity.CENTER else useGravity()
                window.setWindowAnimations(R.style.base_left_dialog_anim)
            }

            DIRECTION_RIGHT -> {
                wlp.gravity = if (useGravity() == -1) Gravity.CENTER else useGravity()
                window.setWindowAnimations(R.style.base_right_dialog_anim)
            }

            DIRECTION_CENTRE -> {
                wlp.gravity = if (useGravity() == -1) Gravity.CENTER else useGravity()
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

    override fun onDestroy() {
        super.onDestroy()
    }


}