package com.v.base.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.InputStream
import java.util.concurrent.TimeUnit


/**
 * author  : ww
 * desc    : 网络请求配置
 * time    : 2022-02-16
 */
class VBNetOptions(builder: Builder) {

    var logEnable: Boolean
    var baseUrl: String?

    var okHttpClient: OkHttpClient.Builder
    var retrofitBuilder: Retrofit.Builder

    var exceptionHandling: VBExceptionHandling

    init {
        this.logEnable = builder.logEnable//是否开启日志打印 默认开启
        this.baseUrl = builder.baseUrl//baseUrl
        this.okHttpClient = builder.okHttpClient
        this.retrofitBuilder = builder.retrofitBuilder
        this.exceptionHandling = builder.exceptionHandling
    }

    class Builder {
        internal var logEnable: Boolean = true
        internal var baseUrl: String? = null
        internal var exceptionHandling = VBExceptionHandling()


        var okHttpClient = OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)   //超时时间 连接、读、写
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
            if (logEnable) {
                addInterceptor(VBLogInterceptor())// 日志拦截器
            }
        }

        internal var retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .apply {
                addConverterFactory(VBFastJsonConverterFactory.create())
                addCallAdapterFactory(CoroutineCallAdapterFactory())
            }

        /**
         * 是否开启日志
         */
        fun setLogEnable(logEnable: Boolean): Builder {
            this.logEnable = logEnable
            return this
        }

        /**
         * 设置BaseUrl
         */
        fun setBaseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        /**
         *设置okhttp拦截器
         */
        fun setInterceptor(vararg interceptor: Interceptor): Builder {
            interceptor.forEach {
                okHttpClient.addInterceptor(it)
            }
            return this
        }

        /**
         *配置SSL证书的InputStream
         */
        fun setSslSocketFactoryInputStream(
            inputStream: InputStream
        ): Builder {
            UnsafeOkHttpClient.setCertificates(okHttpClient, inputStream)
            return this
        }

        fun setRetrofitBuilder(retrofitBuilder: Retrofit.Builder): Builder {
            this.retrofitBuilder = retrofitBuilder
            return this
        }

        /**
         * 设置异常处理
         */
        fun setExceptionHandling(exceptionHandling: VBExceptionHandling): Builder {
            this.exceptionHandling = exceptionHandling
            return this
        }

        fun build(): VBNetOptions {
            okHttpClient.build()
            return VBNetOptions(this)
        }
    }
}