package com.v.base.net

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.v.base.VBApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * @Author : ww
 * desc    :
 * time    : 2021/3/25 10:47
 */

class VBRetrofitManager {

    companion object {
        val instance: VBRetrofitManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            VBRetrofitManager()
        }
    }

    private val api by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(VBApplication.getBaseUrl())
            .addConverterFactory(VBFastJsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(VBNetApi::class.java)

    }


    private val okHttpClient by lazy {
        synchronized(VBRetrofitManager::class.java)
        {
            OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(VBLogInterceptor())
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