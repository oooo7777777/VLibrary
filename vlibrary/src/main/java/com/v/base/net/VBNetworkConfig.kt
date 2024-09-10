package com.v.base.net


//直接使用base库里面的网络请求
val vbNetApi: VBNetApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    val netOptions = VBNetworkConfig.options

    if (netOptions.baseUrl.isNullOrEmpty()) {
        throw NullPointerException("baseUrl为空,请设置baseUrl!")
    }
    netOptions.retrofitBuilder.apply {
        baseUrl(netOptions.baseUrl!!)
        client(netOptions.okHttpClient.build())
    }.build().create(VBNetApi::class.java)
}


/**
 * author  : ww
 * desc    : 网络请求配置
 * time    : 2022-02-16
 */
object VBNetworkConfig {

    val options: VBNetOptions
        get() {
            if (op == null) {
                op = VBNetOptions.Builder().build()
            }
            return op!!
        }

    var op: VBNetOptions? = null
    fun init(options: VBNetOptions) {
        this.op = options
    }

}


