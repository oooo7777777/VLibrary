package com.v.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.v.base.dialog.VBLoadingDialog
import com.v.base.utils.vbToast
import com.v.log.util.logI
import java.lang.reflect.ParameterizedType


abstract class VBFragment<VB : ViewDataBinding, VM : VBViewModel> : Fragment() {


    private var isFirstShow = false

    protected lateinit var mContext: AppCompatActivity

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
        if (context is AppCompatActivity) {
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
    open fun onFragmentResume() {
        javaClass.logI()
    }

    /**
     * 对用户不可见
     */
    open fun onFragmentPause() {}


    protected abstract fun initData()

    protected abstract fun createObserver()

    override fun onResume() {
        super.onResume()
        if (!isFirstShow) {
            isFirstShow = true
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
        isFirstShow = false
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
            //activity与fragment共用一个ViewModel 是导致所有依附于activity的fragment都能收到 会弹出多个弹窗
            //所以这里做收到了以后就做一次清除
            mViewModel.loadingChange.showDialog.clean()
        })
        //关闭弹窗
        mViewModel.loadingChange.dismissDialog.observe(this, Observer {
            if (loadDialog.isShowing) {
                loadDialog.dismiss()
            }
        })

        //toast
        mViewModel.loadingChange.showToast.observe(this, Observer {
            it.vbToast()
            //activity与fragment共用一个ViewModel 是导致所有依附于activity的fragment都能收到 会弹出多个弹窗
            //所以这里做收到了以后就做一次清除
            mViewModel.loadingChange.showToast.clean()
        })
    }

    /**
     * 判断当前fragment是否初始化过
     */
    fun isInitialized(): Boolean {
        return isFirstShow
    }

}
