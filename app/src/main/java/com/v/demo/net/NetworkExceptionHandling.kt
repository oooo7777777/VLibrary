package com.v.demo.net

import com.v.base.bean.VBResponse
import com.v.base.net.VBAppException
import com.v.base.net.VBExceptionHandling
import com.v.log.util.logE
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.HttpException
import kotlin.coroutines.cancellation.CancellationException

/**
 * author  : ww
 * desc    : 网络返回的异常处理
 * time    : 2024/9/4 15:14
 */
class NetworkExceptionHandling : VBExceptionHandling() {

    override fun onException(throwable: Throwable): String {
        var msg = ""
        var errorLog = throwable.toString()
        var isCancellationException = false
        when (throwable) {
            is VBAppException -> {
                msg = throwable.errorMsg
                when (throwable.errCode) {
                    //502 Gateway Timeout
                    500, 502, 503, 504 -> {
                        msg = "网络请求错误，请稍后重试(${throwable.errCode})"
                    }
                    //异地登录
                    401, 403 -> {
                        msg = "异地登录"
                    }

                    else -> {
                    }
                }
            }

            is HttpException -> {
                val responseErrorBody = throwable.response()
                msg = "网络连接错误，请稍后重试(${throwable.code()})"
                errorLog = throwable.toString() + "  " + responseErrorBody?.toString()
            }

            is ConnectTimeoutException, is java.net.SocketTimeoutException -> {
                msg = "网络连接超时，请检查当前网络"
            }

            is com.alibaba.fastjson.JSONException -> {
                msg = "数据错误，请联系我们"
            }

            is java.net.ConnectException, is java.net.UnknownHostException -> {
                msg = "网络未连接，请检查网络设置"
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
            logMessage.logE("NetworkExceptionHandling")
        }
        return msg
    }
}

/**
 * 网络请求返回的基类,用于校验数据的成功与否
 */
data class NetworkApiResponse<T>(val errorCode: Int, val errorMsg: String, val data: T) :
    VBResponse<T>() {

    // 这里是示例，wanandroid 网站返回的 错误码为 0 就代表请求成功，请你根据自己的业务需求来编写
    override fun isSuccess() = errorCode == 0

    override fun getResponseCode() = errorCode

    override fun getResponseData() = data

    override fun getResponseMsg() = errorMsg

}