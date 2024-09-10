package com.v.base.net

import android.util.Log
import com.v.log.util.logE
import kotlin.coroutines.cancellation.CancellationException

/**
 * author  : ww
 * desc    :
 * time    : 2024/9/4 15:11
 */
open class VBExceptionHandling {

    /**
     * 异常处理
     * @param throwable
     */
    open fun onException(
        throwable: Throwable
    ): String {
        var msg = ""
        val errorLog = Log.getStackTraceString(throwable)
        var isCancellationException = false
        when (throwable) {
            is VBAppException -> {
                msg = throwable.errorMsg
            }

            //协程被取消的时候 会提示这个异常(是属于正常的,这里为了不误导直接过滤掉)
            is CancellationException -> {
                isCancellationException = true
            }

            else -> {
                msg = throwable.toString()
            }
        }
        if (!isCancellationException) {
            val logMessage = if (errorLog != msg) {
                "$errorLog\n$msg"
            } else {
                errorLog
            }
            logMessage.logE("MrkExceptionHandling")
        }
        return msg
    }
}