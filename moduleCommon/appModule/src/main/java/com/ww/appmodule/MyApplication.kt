package com.ww.appmodule

import com.v.app.BuildConfig
import com.v.base.BaseApplication

open class MyApplication : BaseApplication() {

    override fun isDebug(): Boolean {
        return BuildConfig.LOG_DEBUG
    }

    override fun initData() {


    }

}