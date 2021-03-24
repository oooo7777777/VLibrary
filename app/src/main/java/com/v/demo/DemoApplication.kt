package com.v.demo

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.v.base.BaseApplication
import com.v.base.net.BaseLogInterceptor
import com.v.base.net.FastJsonConverterFactory
import com.v.base.utils.randomColor
import com.v.demo.net.NetworkHeadInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class DemoApplication : BaseApplication() {

    /**
     * 重写此方法 开启日志打印(日志TAG为 PRETTY_LOGGER)
     */
    override fun isDebug(): Boolean {
        return true
    }

    /**
     * 重写此方法 设置全局状态栏颜色
     */
    override fun statusBarColor(): Int {
        return randomColor
    }

    /**
     * 使用 VLibrary库自带的网络请求时候请重写此方法
     * 传入baseUrl
     */
    override fun baseUrl(): String {
        return "https://gank.io/api/v2/"
    }

    /**
     * 使用 VLibrary库自带的网络请求时候请重写此方法
     * 在这里可以对Retrofit.Builder做任意操作，比如添加json解析器
     */
    override fun retrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().apply {
            addConverterFactory(FastJsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }

    /**
     * 使用 VLibrary库自带的网络请求时候请重写此方法
     * 在这里可以添加拦截器
     */
    override fun okHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)   //超时时间 连接、读、写
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(NetworkHeadInterceptor())  //示例：添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            .addInterceptor(BaseLogInterceptor())// 日志拦截器
            .build()
    }

    override fun initData() {


    }

}