package com.v.base

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger


abstract class VBApplication : Application() {

    companion object {
        private lateinit var context: VBApplication
        private var isLog = true

        fun getApplication(): Application {
            return context
        }

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

        isLog = isDebug()
        if (isLog) {
            Logger.addLogAdapter(AndroidLogAdapter())
        }
        initData()
    }

    protected abstract fun initData()


}