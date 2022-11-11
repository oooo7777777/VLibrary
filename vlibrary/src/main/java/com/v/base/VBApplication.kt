package com.v.base

import android.app.Application
import android.content.Context
import com.hjq.language.MultiLanguages
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


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
        if (isLog) {
            Logger.addLogAdapter(AndroidLogAdapter())
        }
        initData()
    }

    protected abstract fun initData()


    override fun attachBaseContext(base: Context) {
        // 绑定语种
        super.attachBaseContext(MultiLanguages.attach(base))
    }
}