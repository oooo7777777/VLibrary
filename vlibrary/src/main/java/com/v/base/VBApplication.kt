package com.v.base

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


abstract class VBApplication : Application() {

    companion object {
        private lateinit var context: VBApplication

        fun getApplication(): Application {
            return context
        }
    }

    /**
     * 是否开启debug模式 关联输出日志
     */
    protected open fun isDebug(): Boolean = true

    override fun onCreate() {
        super.onCreate()
        context = this

        if (isDebug()) {
            Logger.addLogAdapter(AndroidLogAdapter())
        }
        initData()
    }

    protected abstract fun initData()


}