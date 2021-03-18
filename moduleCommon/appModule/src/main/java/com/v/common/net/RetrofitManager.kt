package com.v.common.net


import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.v.app.BuildConfig
import com.v.base.net.BaseLogInterceptor
import com.v.base.net.FastJsonConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


class RetrofitManager {

    companion object {
        val instance: RetrofitManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RetrofitManager()
        }
    }

    private val api by lazy {
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(FastJsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build().create(NetApi::class.java)

    }


    private val okHttpClient by lazy {
        synchronized(RetrofitManager::class.java)
        {
            OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(RequestHeaderInterceptor())
                .addInterceptor(BaseLogInterceptor())
                .build()
        }

    }

    fun <T> create(service: Class<T>?): T {
        if (service == null) {
            throw RuntimeException("Api service is null!")
        }
        return instance.create(service)
    }


    suspend fun get(url: String, map: Map<String, Any>? = null): ApiResponse<Any> {
        return if (map == null) {
            api.get(url)
        } else {
            api.get(url, map)
        }

    }

    suspend fun getDefault(url: String, map: Map<String, Any>? = null): Any {
        return if (map == null) {
            api.getDefault(url)
        } else {
            api.getDefault(url, map)
        }
    }


    @JvmSuppressWildcards
    suspend fun post(url: String, map: Map<String, Any>? = null): ApiResponse<Any> {
        return if (map == null) {
            api.post(url)
        } else {
            api.post(url, map)
        }

    }

    suspend fun postDefault(url: String, map: Map<String, Any>? = null): Any {
        return if (map == null) {
            api.postDefault(url)
        } else {
            api.postDefault(url, map)
        }
    }

}