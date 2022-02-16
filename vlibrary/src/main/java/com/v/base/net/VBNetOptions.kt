package com.v.base.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit



/**
 * author  : ww
 * desc    : 网络请求配置
 * time    : 2022-02-16
 */
class VBNetOptions(builder: Builder) {

    var logEnable: Boolean
    var logTag: String
    var baseUrl: String?
    var isNetToast: Boolean

    var okHttpClient: OkHttpClient
    var retrofitBuilder: Retrofit.Builder


    init {
        this.logEnable = builder.logEnable
        this.logTag = builder.logTag
        this.baseUrl = builder.baseUrl
        this.isNetToast = builder.isNetToast
        this.okHttpClient = builder.okHttpClient
        this.retrofitBuilder = builder.retrofitBuilder
    }

    class Builder {
        internal var logEnable: Boolean = true
        internal var logTag: String = "PRETTY_LOGGER"
        internal var baseUrl: String? = null
        internal var isNetToast: Boolean = true


        internal var okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
            connectTimeout(10, TimeUnit.SECONDS)   //超时时间 连接、读、写
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
            if (logEnable) {
                addInterceptor(VBLogInterceptor(logTag))// 日志拦截器
            }
        }.build()

        internal var retrofitBuilder: Retrofit.Builder = Retrofit.Builder()
            .apply {
                addConverterFactory(VBFastJsonConverterFactory.create())
                addCallAdapterFactory(CoroutineCallAdapterFactory())
            }

        fun setLogEnable(logEnable: Boolean): Builder {
            this.logEnable = logEnable
            return this
        }

        fun setLogTag(logTag: String): Builder {
            this.logTag = logTag
            return this
        }


        fun setBaseUrl(baseUrl: String): Builder {
            this.baseUrl = baseUrl
            return this
        }

        fun setNetToast(isNetToast: Boolean): Builder {
            this.isNetToast = isNetToast
            return this
        }


        fun setOkHttpClient(okHttpClient: OkHttpClient): Builder {
            this.okHttpClient = okHttpClient
            return this
        }

        fun setRetrofitBuilder(retrofitBuilder: Retrofit.Builder): Builder {
            this.retrofitBuilder = retrofitBuilder
            return this
        }


        fun build(): VBNetOptions {
            return VBNetOptions(this)
        }
    }

    override fun toString(): String {
        return "ShineOptions(logEnable=$logEnable, logTag='$logTag', baseUrl=$baseUrl)"
    }
}