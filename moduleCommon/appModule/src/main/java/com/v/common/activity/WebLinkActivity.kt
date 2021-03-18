package com.v.common.activity

import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.orhanobut.logger.Logger
import com.v.app.databinding.AppActivityWebLinkBinding
import com.v.base.BaseActivity
import com.v.base.BlankViewModel
import qiu.niorgai.StatusBarCompat


class WebLinkActivity : BaseActivity<AppActivityWebLinkBinding, BlankViewModel>() {


    override fun toolBarLift(resId: Int, listener: View.OnClickListener) {
        super.toolBarLift(resId, object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (mViewBinding.webView.canGoBack()) {
                    mViewBinding.webView.goBack()
                    return
                } else {
                    finish()
                }
            }

        })
    }


    override fun initData() {
        intent.extras?.let {

            var url = it.getString("url")
            StatusBarCompat.changeToLightStatusBar(mContext)
            initWebView()
            mViewBinding.webView.loadUrl(url)
        }

    }

    override fun createObserver() {
    }


    private fun initWebView() {
        mViewBinding.webView.settings.javaScriptCanOpenWindowsAutomatically =
            true//设置js可以直接打开窗口，如window.open()，默认为false
        mViewBinding.webView.settings.javaScriptEnabled =
            true//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        mViewBinding.webView.settings.setSupportZoom(true) //是否可以缩放，默认true
        mViewBinding.webView.settings.builtInZoomControls = false//是否显示缩放按钮，默认false
        mViewBinding.webView.settings.useWideViewPort = true//设置此属性，可任意比例缩放。大视图模式
        mViewBinding.webView.settings.loadWithOverviewMode =
            true//和setUseWideViewPort(true)一起解决网页自适应问题
        mViewBinding.webView.settings.setAppCacheEnabled(true)//是否使用缓存
        mViewBinding.webView.settings.domStorageEnabled = true//DOM Storage
        mViewBinding.webView.addJavascriptInterface(this, "android")

        mViewBinding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (mViewBinding.progressBar != null) {
                    mViewBinding.progressBar.progress = newProgress
                    mViewBinding.progressBar.visibility = View.VISIBLE
                    if (newProgress == 100) {
                        mViewBinding.progressBar.visibility = View.INVISIBLE
                    }
                }

                super.onProgressChanged(view, newProgress)
            }

        }

    }

    @JavascriptInterface
    fun by_returnback() {
        finish()
    }

    @JavascriptInterface
    fun by_backpack() {
//        RouteUtil.goMyBackpack()
    }

    @JavascriptInterface
    fun by_recharge() {
//        RouteUtil.goMyRemaining()
    }

    @JavascriptInterface
    fun by_convert() {
//        RouteUtil.goMyIncome()
    }

    @JavascriptInterface
    fun by_userInfo(uid: String?) {
//        RouteUtil.forwardUserHome(context, uid)
    }

    @JavascriptInterface
    fun by_h5(tag: String) {
        Logger.e("by_h5:$tag")

        if (tag.contains("boya_navigation")) //带title的web
        {
            super.showTitleBar(View.VISIBLE)
        } else //不带title的web
        {
            super.showTitleBar(View.GONE)
            StatusBarCompat.translucentStatusBar(mContext)
        }
        StatusBarCompat.changeToLightStatusBar(mContext)
    }

    override fun onBackPressed() {
        //监听返回键，如果可以后退就后退
        if (mViewBinding.webView.canGoBack()) {
            mViewBinding.webView.goBack()
        } else {
            finish()
        }
    }


}