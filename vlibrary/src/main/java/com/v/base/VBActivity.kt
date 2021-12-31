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
import com.v.base.utils.getApplicationViewModel
import com.v.base.utils.isWhiteColor
import kotlinx.android.synthetic.main.vb_title_bar.view.*
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType


abstract class VBActivity<VB : ViewDataBinding, VM : VBViewModel> : AppCompatActivity() {


    lateinit var mContext: AppCompatActivity


    val mTitleBar by lazy {
        mRootDataBinding.vbTitleBar
    }

    val mRootDataBinding by lazy {
        mContext.vbGetDataBinding<VbRootLayoutBinding>(R.layout.vb_root_layout)
    }


    /**
     * 通过反射拿到ViewModel并且注册
     */
    protected val mViewModel: VM by lazy {
        val type = javaClass.genericSuperclass as ParameterizedType
        val aClass = type.actualTypeArguments[1] as Class<VM>
        if (useViewModelApplication()) {
            getApplicationViewModel(application, aClass)

        } else {
            ViewModelProvider(this).get(aClass)
        }

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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        mContext = this

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
        initToolBar()

        initData()
        createObserver()
        javaClass.name.log()
    }


    private fun initToolBar() {

        statusBarColor()
        mTitleBar.useStatusBar(useStatusBar())

        showTitleBar(useTitleBar())

        toolBarTitle()
        toolBarLift()
        toolBarRight()
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

        mTitleBar.ivStatusBar.vbSetViewLayoutParams(h = vbGetStatusBarHeight())
        mTitleBar.setStatusBarColor(color)
    }

    /**
     * 显示Toolbar
     * @param title 文字 [title]不为空才会显示TitleBar
     * @param titleColor 文字颜色
     * @param isShowBottomLine 是否显示Toolbar下面的分割线
     * @param listener 点击事件
     */
    protected open fun toolBarTitle(
        title: String = "",
        titleColor: Int = Color.BLACK,
        isShowBottomLine: Boolean = true,
        listener: View.OnClickListener? = null
    ) {
        if (title.isNullOrEmpty()) {
            mTitleBar.useToolbar(false)
        } else {
            mTitleBar.setTitle(title, titleColor, isShowBottomLine, listener)
            mTitleBar.useToolbar(true)
        }
    }


    /**
     * 设置Toolbar左边 (图片,文字只能显示一个,图片优先)
     * @param resId 图片资源
     * @param text 文字
     * @param textColor 文字颜色
     * @param listener 点击事件
     */
    protected open fun toolBarLift(
        resId: Int = R.mipmap.vb_ic_back_black,
        text: String = "",
        textColor: Int = Color.BLACK,
        listener: View.OnClickListener = View.OnClickListener { mContext.onBackPressed() }
    ) {

        if (resId == 0) {
            mTitleBar.setLeft(text, textColor, listener)
        } else {
            mTitleBar.setLeft(resId, listener)
        }

    }


    /**
     * 设置Toolbar右边 (图片,文字只能显示一个,图片优先)
     * @param resId 图片资源
     * @param text 文字
     * @param textColor 文字颜色
     * @param listener 点击事件
     */
    protected open fun toolBarRight(
        resId: Int = 0,
        text: String = "",
        textColor: Int = Color.BLACK,
        listener: View.OnClickListener? = null
    ) {
        if (resId == 0) {
            mTitleBar.setRight(text, textColor, listener)
        } else {
            mTitleBar.setRight(resId, listener)
        }
    }


    /**
     * 显示Toolbar
     */
    protected open fun showTitleBar(show: Boolean = useTitleBar()) {
        mTitleBar.useToolbar(show)
    }

    /**
     * 设置是否显示Toolbar
     */
    protected open fun useTitleBar(): Boolean = true

    /**
     * 是否显示状态栏
     */
    protected open fun useStatusBar(): Boolean = true

    /**
     * 是否addView vb的布局
     */
    protected open fun useAddViewVBRoot(): Boolean = true

    /**
     * ViewModel是否绑定Application生命周期
     */
    protected open fun useViewModelApplication(): Boolean = false

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