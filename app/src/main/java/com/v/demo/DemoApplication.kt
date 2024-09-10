package com.v.demo

import com.v.base.VBApplication
import com.v.base.VBConfigOptions
import com.v.base.net.VBNetOptions
import com.v.base.net.VBNetworkConfig
import com.v.base.VBConfig
import com.v.demo.net.NetworkExceptionHandling
import com.v.demo.net.NetworkHeadInterceptor
import com.v.log.LogConfig

class DemoApplication : VBApplication() {

    override fun logConfig(): LogConfig {
        return LogConfig(this, true, true)
    }

    override fun initData() {

        //基类配置
        val vbOptions = VBConfigOptions.Builder()
            .build()
        VBConfig.init(vbOptions)

        //网络请求配置
        val netOptions = VBNetOptions.Builder()
            .setBaseUrl("https://www.wanandroid.com/")
            .setInterceptor(NetworkHeadInterceptor())
            .setExceptionHandling(NetworkExceptionHandling())
            .build()

        VBNetworkConfig.init(netOptions)
    }

}