package com.v.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.hjq.language.MultiLanguages
import com.hjq.toast.Toaster
import com.v.log.LogConfig
import com.v.log.VLog


abstract class VBApplication : Application() {

    companion object {
        private lateinit var context: VBApplication
        fun getApplication(): Application = context
    }
    /**
     * 日志配置
     */
    protected open fun logConfig(): LogConfig? = LogConfig(this, true)

    override fun onCreate() {
        super.onCreate()
        context = this
        // 初始化语种切换框架
        MultiLanguages.init(this)
        //初始化日志
        logConfig()?.run {
            VLog.init(this)
        }
        // 初始化 Toast 框架
        Toaster.init(this)
        initData()
    }

    protected abstract fun initData()

    override fun attachBaseContext(base: Context) {
        // 绑定语种
        super.attachBaseContext(MultiLanguages.attach(base))
    }
}