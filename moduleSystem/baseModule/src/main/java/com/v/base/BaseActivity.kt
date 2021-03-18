package com.v.base

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.noober.background.BackgroundLibrary
import com.v.base.databinding.BaseLayoutBinding
import com.v.base.utils.ActivityManager
import com.v.base.utils.log
import com.v.base.utils.logD
import qiu.niorgai.StatusBarCompat
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel> : AppCompatActivity() {

    private lateinit var mBaseViewBinding: BaseLayoutBinding

    lateinit var mContext: AppCompatActivity

    /**
     * 通过反射拿到ViewModel并且注册
     */
    protected val mViewModel: VM by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[1] as Class<VM>
        ViewModelProvider(this).get(aClass)
    }

    /**
     * 通过反射拿到ViewBinding
     */
    protected val mViewBinding: VB by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(null, layoutInflater) as VB
    }


    /**
     * 初始化加载框
     */
    private val loadDialog by lazy {
        LoadingDialog(this).setDialogCancelable(false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        //注册BackgroundLibrary 可以直接在xml里面写shape
        BackgroundLibrary.inject2(this)
        super.onCreate(savedInstanceState)
        //添加当前activity 统一管理
        ActivityManager.appManager.addActivity(this)
        //设置屏幕反向为竖向
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mContext = this
        //获取base ViewBinding
        mBaseViewBinding = DataBindingUtil.setContentView(
            this,
            R.layout.base_layout
        )
        val rootView: View = mBaseViewBinding.root
        //把mViewBinding添加到base ViewBinding里面去
        mBaseViewBinding.layoutContent.addView(mViewBinding.root)
        super.setContentView(rootView)
        //注册加载框
        registerUiChange()
        //设置TitleBar
        showTitleBar(showTitleBar())

        if (useTranslucentStatusBar()) {
            mBaseViewBinding.ivStatusBar.visibility = View.GONE
        }
        if (useLightBar()) {
            StatusBarCompat.changeToLightStatusBar(mContext)
        } else {
            StatusBarCompat.cancelLightStatusBar(mContext)
        }
        toolBarTitle(
            "", ContextCompat.getColor(
                mContext, R.color.base_colorTheme
            )
        )
        statusBarColor()
        toolBarLift(0)
        toolBarRight("", 0, null)
        toolBarRight(0, null)
        initData()
        createObserver()
        javaClass.name.log()
    }


    /**
     * 设置状态栏颜色
     * @param color 颜色
     */
    protected open fun statusBarColor(
        color: Int = ContextCompat.getColor(
            mContext,
            R.color.base_black
        )
    ) {
        mBaseViewBinding.ivStatusBar.setBackgroundColor(color)
    }

    /**
     * 设置TitleBar左边图片
     * @param resId 图片
     * @param listener 点击事件(默认是点击返回)
     */
    protected open fun toolBarLift(
        resId: Int = R.mipmap.base_icon_back_black,
        listener: View.OnClickListener = View.OnClickListener { mContext.onBackPressed() }
    ) {

        mBaseViewBinding.ivLeft?.run {
            setImageResource(resId)
            setOnClickListener(listener)
        }

    }

    /**
     * 设置TitleBar右边文字
     * @param text 文字
     * @param textColor 文字颜色
     * @param listener 点击事件
     */
    protected open fun toolBarRight(text: String, textColor: Int, listener: View.OnClickListener?) {
        mBaseViewBinding.tvRight?.run {
            setText(text)
            setTextColor(textColor)
            setOnClickListener(listener)
        }
    }

    /**
     * 设置TitleBar右边图片
     * @param resId 图片资源
     * @param listener 点击事件
     */
    protected open fun toolBarRight(resId: Int, listener: View.OnClickListener?) {
        mBaseViewBinding.ivRight?.run {
            setImageResource(resId)
            setOnClickListener(listener)
        }

    }

    /**
     * 显示TitleBar [title]不为空才会显示TitleBar
     * @param title TitleBar文字
     * @param titleColor TitleBar文字颜色
     */
    protected open fun toolBarTitle(
        title: String = "",
        titleColor: Int = ContextCompat.getColor(
            mContext, R.color.base_black
        )
    ) {
        if (title.isNotEmpty()) {
            mBaseViewBinding.toolbar.visibility = View.VISIBLE
        } else {
            mBaseViewBinding.toolbar.visibility = View.GONE
        }
        mBaseViewBinding.tvTitle?.run {
            text = title
            setTextColor(titleColor)
        }
    }

    /**
     * 显示TitleBar
     */
    protected open fun showTitleBar(visible: Int) {
        mBaseViewBinding.toolbar.visibility = visible
    }

    /**
     * 显示TitleBar
     */
    protected open fun showTitleBar(): Int = View.VISIBLE

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * liveData 数据监听
     */
    protected abstract fun createObserver()

    /**
     * 是否全透明状态栏
     */
    protected open fun useTranslucentStatusBar(): Boolean = false

    /**
     * 状态栏文字 是否深色
     */
    protected open fun useLightBar(): Boolean = false

    override fun onDestroy() {
        super.onDestroy()
        ActivityManager.appManager.finishActivity(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragmentManager = supportFragmentManager
        for (i in fragmentManager.fragments.indices) {
            val fragment = fragmentManager.fragments[i] //找到第一层Fragment
            fragment?.let { handleResult(it, requestCode, resultCode, data) }
        }
    }

    /**
     * 递归调用，对所有的子Fragment生效
     * @param fragment
     * @param requestCode
     * @param resultCode
     * @param data
     */
    private fun handleResult(fragment: Fragment, requestCode: Int, resultCode: Int, data: Intent?) {
        fragment.onActivityResult(requestCode, resultCode, data) //调用每个Fragment的onActivityResult
        val childFragment = fragment.childFragmentManager.fragments //找到第二层Fragment
        if (childFragment != null) {
            for (f in childFragment) f?.let { handleResult(it, requestCode, resultCode, data) }
        }

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


}