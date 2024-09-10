package com.v.base.net

import com.v.base.R
import com.v.base.VBApplication
import com.v.base.annotaion.VBError


/**
 * author  : ww
 * desc    : 错误日志处理
 * time    : 2021-03-16 09:52:45
 */
class VBAppException(//错误码
    var errCode: Int, error: String?, errorLog: String? = ""
) : Exception(error) {

    var errorMsg: String //错误消息
    var errorLog: String? //错误日志

    init {
        this.errorMsg = if (error.isNullOrEmpty()) {
            ""
        } else {
            error.toString()
        }
        this.errorLog = if (errorLog.isNullOrEmpty()) {
            this.errorMsg
        } else {
            errorLog.toString()
        }
    }


    override fun toString(): String {
        return "errCode=$errCode errorMsg=$errorMsg errorLog=$errorLog"
    }
}