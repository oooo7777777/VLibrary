package com.v.demo

import com.hjq.language.MultiLanguages
import com.hjq.language.OnLanguageListener
import com.v.base.VBApplication
import com.v.base.VBConfig
import com.v.base.VBConfigOptions
import com.v.base.net.VBNetOptions
import com.v.log.LogConfig
import com.v.log.util.log
import java.util.*

class DemoApplication : VBApplication() {

    override fun logConfig(): LogConfig {
        return LogConfig(this, true)
    }

    override fun initData() {
        val options = VBConfigOptions.Builder()
            .setNetOptions(
                VBNetOptions.Builder()
                    .setBaseUrl("https://www.wanandroid.com/")
                    .build()
            )
            .build()
        VBConfig.init(options)


        // 设置语种变化监听器
        MultiLanguages.setOnLanguageListener(object : OnLanguageListener {
            override fun onAppLocaleChange(oldLocale: Locale, newLocale: Locale) {
                ("监听到应用切换了语种，旧语种：$oldLocale，新语种：$newLocale").log()
            }

            override fun onSystemLocaleChange(oldLocale: Locale, newLocale: Locale) {
                ("监听到系统切换了语种，旧语种：" + oldLocale + "，新语种：" + newLocale + "，是否跟随系统：" + MultiLanguages.isSystemLanguage()).log()
            }
        })

    }

}