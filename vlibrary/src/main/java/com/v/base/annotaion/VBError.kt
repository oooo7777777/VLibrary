package com.v.base.annotaion

import com.v.base.R
import com.v.base.VBApplication


enum class VBError(private val code: Int, private val err: String) {

    /**
     * 未知错误
     */
    UNKNOWN(941000, VBApplication.getApplication().getString(R.string.vb_string_error_941000)),

    /**
     * 解析错误
     */
    PARSE_ERROR(941001, VBApplication.getApplication().getString(R.string.vb_string_error_941001)),

    /**
     * 网络错误
     */
    NETWORK_ERROR(941002, VBApplication.getApplication().getString(R.string.vb_string_error_941002)),

    /**
     * 证书出错
     */
    SSL_ERROR(941003, VBApplication.getApplication().getString(R.string.vb_string_error_941003)),

    /**
     * 连接超时
     */
    TIMEOUT_ERROR(941004, VBApplication.getApplication().getString(R.string.vb_string_error_941004));

    fun getValue(): String {
        return err
    }

    fun getKey(): Int {
        return code
    }

}