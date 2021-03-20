package com.v.base

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.alibaba.android.arouter.launcher.ARouter
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout


abstract class BaseApplication : Application() {


    companion object {
        private var context: BaseApplication? = null
        fun getContext(): Context {
            return BaseApplication.Companion.context!!
        }
    }

    /**
     *    是否开启debug模式 关联输出日志
     */
    protected open fun isDebug(): Boolean = true


    override fun onCreate() {
        super.onCreate()
        context = this

        if (isDebug()) {
            Logger.addLogAdapter(AndroidLogAdapter())

        }
        initARouter()
        initSmartRefreshLayout()
        initData()

    }

    /**
     * 初始化 ARouter
     */
    private fun initARouter() {
        if (isDebug()) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

    /**
     * 全局设置SmartRefreshLayout
     */
    private fun initSmartRefreshLayout() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            MaterialHeader(context)
        }

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context)
        }
    }


    protected abstract fun initData()


}