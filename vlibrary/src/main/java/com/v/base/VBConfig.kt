package com.v.base

/**
 * author  : ww
 * desc    : 网络请求配置
 * time    : 2022-02-16
 */
object VBConfig {

    val options: VBConfigOptions
        get() {
            if (op == null) {
                op = VBConfigOptions.Builder().build()
            }
            return op!!
        }

    var op: VBConfigOptions? = null
    fun init(options: VBConfigOptions) {
        op = options
    }

}


