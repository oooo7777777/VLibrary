package com.v.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.noober.background.BackgroundLibrary
import com.v.base.databinding.VbRootLayoutBinding
import com.v.base.dialog.VBLoadingDialog
import com.v.base.utils.ext.*
import com.v.base.utils.isWhiteColor
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType


abstract class VBActivity<VB : ViewDataBinding, VM : VBViewModel> : AppCompatActivity() {

    lateinit var mRootDataBinding: VbRootLayoutBinding

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
     * 通过反射拿到DataBinding
     */
    protected val mDataBinding: VB by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[0] as Class<*>
        val method = aClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        method.invoke(null, layoutInflater) as VB
    }

    /**
     * 初始化加载框
     */
    private val loadDialog by lazy {
        VBLoadingDialog(this).setDialogCancelable(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //注册BackgroundLibrary 可以直接在xml里面写shape
        BackgroundLibrary.inject2(this)
        super.onCreate(savedInstanceState)
        //设置屏幕竖向
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mContext = this
        //获取base DataBinding
        mRootDataBinding = DataBindingUtil.setContentView(
            this,
            R.layout.vb_root_layout
        )
        val rootView: View = mRootDataBinding.root
        //把mDataBinding添加到baseDataBinding里面去
        if (useAddViewVBRoot()) {
            mRootDataBinding.layoutContent.addView(mDataBinding.root)
        }
        super.setContentView(rootView)

        mRootDataBinding.lifecycleOwner = this
        mDataBinding.lifecycleOwner = this

        //注册加载框
        registerUiChange()

        //设置Toolbar
        showTitleBar(useTitleBar())

        toolBarTitle(
            "", Color.BLACK
        )
        statusBarColor()
        toolBarLift()
        toolBarRight("", 0, null)
        toolBarRight(0, null)
        //设置是否隐藏状态栏
        mRootDataBinding.ivStatusBar.visibility = if (useStatusBar()) View.GONE else View.VISIBLE
        initData()
        createObserver()
        javaClass.name.log()
    }


    /**
     * 设置状态栏颜色
     * @param color 颜色
     */
    protected open fun statusBarColor(
        color: Int = VBApplication.getStatusBarColor()
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                val decorViewClazz = Class.forName("com.android.internal.policy.DecorView")
                val field: Field = decorViewClazz.getDeclaredField("mSemiTransparentStatusBarColor")
                field.isAccessible = true
                field.setInt(window.decorView, Color.TRANSPARENT) //改为透明
            } catch (e: Exception) {
            }
        }

        //状态栏颜色趋近于白色时，会智能将状态栏字体颜色变换为黑色
        if (isWhiteColor(color)) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR//黑色
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE//白色
        }

        mRootDataBinding.ivStatusBar.vbViewLayoutParams(h = vbStatusBarHeight())
        mRootDataBinding.ivStatusBar.setBackgroundColor(color)
    }


    /**
     * 设置Toolbar左边图片
     * @param resId 图片
     * @param listener 点击事件(默认是点击返回)
     */
    protected open fun toolBarLift(
        resId: Int = R.mipmap.vb_ic_back_black,
        listener: View.OnClickListener = View.OnClickListener { mContext.onBackPressed() }
    ) {
        mRootDataBinding.ivLeft?.run {
            setImageResource(resId)
            this.vbOnClickAnimator() {
                listener.onClick(it)
            }

        }

    }

    /**
     * 设置Toolbar右边文字
     * @param text 文字
     * @param textColor 文字颜色
     * @param listener 点击事件
     */
    protected open fun toolBarRight(text: String, textColor: Int, listener: View.OnClickListener?) {
        mRootDataBinding.tvRight?.run {
            setText(text)
            setTextColor(textColor)
            setOnClickListener(listener)
        }
    }

    /**
     * 设置Toolbar右边图片
     * @param resId 图片资源
     * @param listener 点击事件
     */
    protected open fun toolBarRight(resId: Int, listener: View.OnClickListener?) {
        mRootDataBinding.ivRight?.run {
            setImageResource(resId)
            setOnClickListener(listener)
        }

    }

    /**
     * 显示Toolbar [title]不为空才会显示TitleBar
     * @param title TitleBar文字
     * @param titleColor TitleBar文字颜色
     */
    protected open fun toolBarTitle(
        title: String = "",
        titleColor: Int = Color.BLACK
    ) {
        if (title.isNotEmpty()) {
            mRootDataBinding.toolbar.visibility = View.VISIBLE
        } else {
            mRootDataBinding.toolbar.visibility = View.GONE
        }
        mRootDataBinding.tvTitle?.run {
            text = title
            setTextColor(titleColor)
        }
    }

    /**
     * 显示Toolbar
     */
    protected open fun showTitleBar(visible: Int) {
        mRootDataBinding.toolbar.visibility = visible
    }

    /**
     * 设置是否显示Toolbar
     */
    protected open fun useTitleBar(): Int = View.VISIBLE

    /**
     * 设置隐藏状态栏(布局需要顶入状态栏的时候使用)
     */
    protected open fun useStatusBar(): Boolean = false

    /**
     * 是否addView vb的布局
     */
    protected open fun useAddViewVBRoot(): Boolean = true

    /**
     * 初始化数据
     */
    protected abstract fun initData()

    /**
     * liveData 数据监听
     */
    protected abstract fun createObserver()


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