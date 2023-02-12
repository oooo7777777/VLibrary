package com.v.base.net

import com.v.base.R
import com.v.base.VBApplication
import com.v.base.annotaion.VBError


/**
 * author  : ww
 * desc    : 错误日志处理
 * time    : 2021-03-16 09:52:45
 */
class VBAppException : Exception {

    var errorMsg: String //错误消息
    var errCode: Int = 0 //错误码
    var errorLog: String? //错误日志

    constructor(errCode: Int, error: String?, errorLog: String? = "") : super(error) {

        this.errorMsg = if (error.isNullOrEmpty()) {
            VBApplication.getApplication().getString(R.string.vb_string_error_941000)
        } else {
            error.toString()
        }

        this.errorLog = if (errorLog.isNullOrEmpty()) {
            this.errorMsg
        } else {
            errorLog.toString()
        }

        this.errCode = errCode
    }

    constructor(error: VBError, e: Throwable?) {
        errCode = error.getKey()
        errorMsg = error.getValue()
        errorLog = e?.message
    }

    override fun toString(): String {
        return "errCode=$errCode errorMsg=$errorMsg errorLog=$errorLog"
    }
}