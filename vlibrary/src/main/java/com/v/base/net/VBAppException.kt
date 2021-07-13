package com.v.base.net

import com.v.base.annotaion.VBError
import com.v.base.utils.ext.otherwise
import com.v.base.utils.ext.yes


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
        this.errorMsg = error.isNullOrEmpty().yes {
            "请求失败，请稍后再试"
        }.otherwise {
            error.toString()
        }

        this.errorLog = errorLog.isNullOrEmpty().yes {
            this.errorMsg
        }.otherwise {
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
        return "errCode=$errCode\nerrorMsg=$errorMsg\nerrorLog=$errorLog"

    }
}