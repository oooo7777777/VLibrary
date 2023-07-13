package com.v.demo

import com.v.base.VBApplication
import com.v.base.VBConfig
import com.v.base.VBConfigOptions
import com.v.base.net.VBNetOptions
import com.v.base.utils.vbGetScreenHeight
import com.v.base.utils.vbGetStatusBarHeight
import com.v.log.LogConfig

class DemoApplication : VBApplication() {

    override fun logConfig(): LogConfig? {
        return LogConfig(this, true)
    }

    override fun initData() {
        val options = VBConfigOptions.Builder()
            .setNetOptions(
                VBNetOptions.Builder()
                    .setBaseUrl("https://www.wanandroid.com/")
                    .build()
            )
            .setScreenWidth(375f)
            .setScreenHeight(812f - this.vbGetStatusBarHeight())
            .build()
        VBConfig.init(options)
    }

}