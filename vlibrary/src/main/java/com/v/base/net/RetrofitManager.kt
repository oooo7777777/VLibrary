package com.v.base.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.v.base.BaseApplication
import com.v.base.utils.toBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @Author : ww
 * desc    :
 * time    : 2021/3/25 10:47
 */

class RetrofitManager {

    companion object {
        val instance: RetrofitManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManager()
        }
    }

    private val api by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BaseApplication.getBaseUrl())
            .addConverterFactory(FastJsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(BaseNetApi::class.java)

    }


    private val okHttpClient by lazy {
        synchronized(RetrofitManager::class.java)
        {
            OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(BaseLogInterceptor())
                .build()
        }

    }


    suspend fun get(url: String, map: Map<String, Any>? = null): String {
        return if (map == null) {
            api.get(url)
        } else {
            api.get(url, map)
        }
    }

    suspend fun post(url: String, map: Map<String, Any>? = null): Any {
        return if (map == null) {
            api.post(url)
        } else {
            api.post(url, map)
        }
    }

}