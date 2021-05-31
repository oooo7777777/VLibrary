package com.v.base

import android.app.Application
import android.content.Context
import android.graphics.Color
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.v.base.net.BaseLogInterceptor
import com.v.base.net.BaseNetApi
import com.v.base.net.FastJsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


abstract class BaseApplication : Application() {


    companion object {
        private lateinit var context: BaseApplication

        private var baseUrl: String? = null
        private var builder: Retrofit.Builder? = null

        private var statusBarColor: Int = 0

        fun getContext(): Context {
            return context
        }

        fun getRetrofitBuilder(): Retrofit.Builder? {
            return builder
        }

        fun getBaseUrl(): String? {
            return baseUrl
        }

        fun getStatusBarColor():Int
        {
            return statusBarColor
        }

        //直接使用base库里面的网络请求
        val apiBase: BaseNetApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            if (getBaseUrl().isNullOrEmpty()) {
                throw IllegalStateException("baseUrl为空,请继承BaseApplication重写baseUrl()")
            }
            getRetrofitBuilder()?.build()!!.create(BaseNetApi::class.java)
        }
    }


    /**
     * 重写父类的okHttpClient方法，
     * 在这里可以添加拦截器
     */
    protected open fun okHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(10, TimeUnit.SECONDS)   //超时时间 连接、读、写
        readTimeout(5, TimeUnit.SECONDS)
        writeTimeout(5, TimeUnit.SECONDS)
        addInterceptor(BaseLogInterceptor())// 日志拦截器
    }.build()

    /**
     * 重写父类的retrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加json解析器
     */
    protected open fun retrofitBuilder(): Retrofit.Builder = Retrofit.Builder()
        .apply {
            addConverterFactory(FastJsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }

    /**
     * 使用 VLibrary库自带的网络请求时候请重写此方法
     */
    protected open fun baseUrl(): String = ""

    /**
     * 设置状态栏颜色
     */
    protected open fun statusBarColor(): Int = Color.parseColor("#000000")

    /**
     * 是否开启debug模式 关联输出日志
     */
    protected open fun isDebug(): Boolean = true


    override fun onCreate() {
        super.onCreate()
        context = this
        statusBarColor = statusBarColor()

        if (isDebug()) {
            Logger.addLogAdapter(AndroidLogAdapter())
        }

        if (!baseUrl().isNullOrEmpty()) {
            baseUrl = baseUrl()
            builder = retrofitBuilder().apply {
                baseUrl(baseUrl)
                client(okHttpClient())
            }

        }

        initSmartRefreshLayout()
        initData()

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