package com.v.base

import android.app.Application
import android.content.Context
import com.hjq.language.MultiLanguages
import com.v.log.LogConfig
import com.v.log.VLog


abstract class VBApplication : Application() {

    companion object {
        private lateinit var context: VBApplication
        private var isLog = true

        fun getApplication(): Application = context

        fun isLog(): Boolean {
            return isLog
        }
    }

    /**
     * 是否开启debug模式 关联输出日志
     */
    protected open fun isDebug(): Boolean = true

    override fun onCreate() {
        super.onCreate()
        context = this

        // 初始化语种切换框架
        MultiLanguages.init(this)

        isLog = isDebug()
        VLog.init(LogConfig(this, isLog))

        initData()
    }

    protected abstract fun initData()


    override fun attachBaseContext(base: Context) {
        // 绑定语种
        super.attachBaseContext(MultiLanguages.attach(base))
    }
}