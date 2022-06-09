package com.v.demo.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.v.base.net.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @Author : ww
 * desc    :
 * time    : 2021/3/20 11:01O
 */


class Network : VBNetwork() {

    companion object {

        //自定义的api请求
        val apiService: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            instance.getApi(NetworkApi::class.java, NetworkApi.SERVER_URL)
        }

        private val instance: Network by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Network()
        }

    }

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //示例：添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            addInterceptor(NetworkHeadInterceptor())
            // 日志拦截器
            addInterceptor(VBLogInterceptor())
            //超时时间 连接、读、写
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
        }
        return builder
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，protobuf等
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(VBFastJsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }

}



