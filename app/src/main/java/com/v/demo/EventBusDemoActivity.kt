package com.v.demo

import android.view.View
import androidx.lifecycle.Observer
import com.v.base.BaseActivity
import com.v.base.BlankViewModel
import com.v.base.utils.LiveDataBus
import com.v.base.utils.getApplicationViewModel
import com.v.base.utils.randomNumber
import com.v.demo.databinding.ActivityEventBusDemoBinding
import com.v.demo.model.AppViewModel

/**
 * author  :
 * desc    : 通过liveData 模拟eventBus
 * time    : 2021-03-19 10:28:14
 */
class EventBusDemoActivity : BaseActivity<ActivityEventBusDemoBinding, BlankViewModel>(),
    View.OnClickListener {


    /**
     * 设置显示Toolbar
     * @param title 标题(默认是空字符串 空字符则表示不显示Toolbar)
     * @param title 标题颜色
     */
    override fun toolBarTitle(title: String, titleColor: Int) {
        super.toolBarTitle(title, titleColor)
    }

    /**
     * 设置Toolbar左边按钮事件
     * @param resId 按钮图片资源(默认箭头)
     * @param listener 按钮事件(默认onBackPressed)
     */
    override fun toolBarLift(resId: Int, listener: View.OnClickListener) {
        super.toolBarLift(resId, listener)
    }

    /**
     * 设置Toolbar右边按钮图片事件
     * @param resId 按钮资源
     * @param listener 按钮事件
     */
    override fun toolBarRight(resId: Int, listener: View.OnClickListener?) {
        super.toolBarRight(resId, listener)
    }

    /**
     * 设置Toolbar右边按钮文字事件
     * @param text 按钮资源
     * @param listener 按钮事件
     */
    override fun toolBarRight(text: String, textColor: Int, listener: View.OnClickListener?) {
        super.toolBarRight(text, textColor, listener)
    }

    /**
     * 设置状态栏颜色
     * @param color 颜色资源
     */
    override fun statusBarColor(color: Int) {
        super.statusBarColor(color)
    }

    /**
     * 设置是否隐藏状态栏(布局需要顶入状态栏的时候使用)
     */
    override fun useStatusBar(): Boolean {
        return super.useStatusBar()
    }

    /**
     * 设置是否显示Toolbar
     */
    override fun useTitleBar(): Int {
        return super.useTitleBar()
    }


    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {

        //全局数据监听
        getApplicationViewModel(mContext.application, AppViewModel::class.java).string.observe(
            this,
            androidx.lifecycle.Observer {
                mViewBinding.tvContent.text = it
            })


        //单项数据监听
        LiveDataBus.with<String>(ConstData.CONTENT).observe(this, Observer {
            mViewBinding.tvContent.text = it
        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            mViewBinding.bt0.id -> {

                getApplicationViewModel(
                    mContext.application,
                    AppViewModel::class.java
                ).string.postValue("我是全局数据" + randomNumber(99))

            }
            mViewBinding.bt1.id -> {
                LiveDataBus.with<String>(ConstData.CONTENT)
                    .postData("模拟eventBus" + randomNumber(99))
            }
        }
    }

}