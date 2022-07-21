package com.v.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.v.base.dialog.VBLoadingDialog
import com.v.base.utils.log
import com.v.base.utils.toast
import java.lang.reflect.ParameterizedType


abstract class VBFragment<VB : ViewDataBinding, VM : VBViewModel> : Fragment() {


    private var isFirstShow = true

    protected lateinit var mContext: Activity

    protected lateinit var mDataBinding: VB


    private val loadDialog by lazy {
        VBLoadingDialog(mContext).setCanceled(false)
    }


    protected val mViewModel: VM by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[1] as Class<VM>
        aClass.getDeclaredConstructor().isAccessible = true
        if (viewModelSyn()) {
            ViewModelProvider(requireActivity()).get(aClass)
        } else {
            ViewModelProvider(this).get(aClass)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            this.mContext = context
        }
    }


    override fun onCreateView(
        @NonNull inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java
        )
        method.isAccessible = true//关掉安全检查
        mDataBinding = method.invoke(null, layoutInflater, container, false) as VB
        mDataBinding.lifecycleOwner = this

        return mDataBinding.root
    }


    /**
     * viewModel是否使用当前fragment所依赖的activity创建 可以viewModel数据共享
     */
    protected open fun viewModelSyn(): Boolean = false

    /**
     * 对用户可见
     */
    open fun onFragmentResume() {}

    /**
     * 对用户不可见
     */
    open fun onFragmentPause() {}


    protected abstract fun initData()

    protected abstract fun createObserver()

    override fun onResume() {
        super.onResume()
        javaClass.log()
        if (isFirstShow) {
            isFirstShow = false
            registerUiChange()
            initData()
            createObserver()
        }
        onFragmentResume()
    }

    override fun onPause() {
        super.onPause()
        onFragmentPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDataBinding.unbind()
        isFirstShow = true
    }


    /**
     * 注册UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.loadingChange.showDialog.observe(this, Observer {
            if (!loadDialog.isShowing) {
                loadDialog.show()
                loadDialog.setMsg(it)
            }
        })
        //关闭弹窗
        mViewModel.loadingChange.dismissDialog.observe(this, Observer {
            if (loadDialog.isShowing) {
                loadDialog.dismiss()
            }

        })

        //toast
        mViewModel.loadingChange.showToast.observe(this, Observer {
            it.toast()
        })
    }


}
