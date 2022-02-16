package com.v.base

import com.v.base.net.VBNetApi


//直接使用base库里面的网络请求
val apiBase: VBNetApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    val netOptions = VBConfig.options.netOptions

    if (netOptions.baseUrl.isNullOrEmpty()) {
        throw IllegalStateException("baseUrl为空,请继承BaseApplication重写baseUrl()")
    }
    netOptions.retrofitBuilder.apply {
        baseUrl(netOptions.baseUrl)
        client(netOptions.okHttpClient)
    }.build().create(VBNetApi::class.java)
}

/**
 * author  : ww
 * desc    : 网络请求配置
 * time    : 2022-02-16
 */
object VBConfig {

    var options: VBConfigOptions = VBConfigOptions.Builder().build()

    fun init(options: VBConfigOptions) {
        this.options = options
    }

}


