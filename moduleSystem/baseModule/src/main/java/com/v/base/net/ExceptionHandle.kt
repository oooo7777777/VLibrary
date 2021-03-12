package com.v.base.net

import com.v.base.utils.ext.logE
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException


object ExceptionHandle {

    fun handleException(e: Throwable): AppException {
        val ex: AppException
        e.let {
            when (it) {
                is HttpException -> {
                    ex = AppException(Error.NETWORK_ERROR, e)
                }
                is ConnectException -> {
                    ex = AppException(Error.NETWORK_ERROR, e)
                }
                is javax.net.ssl.SSLException -> {
                    ex = AppException(Error.SSL_ERROR, e)
                }
                is ConnectTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                }
                is java.net.SocketTimeoutException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)
                }
                is java.net.UnknownHostException -> {
                    ex = AppException(Error.TIMEOUT_ERROR, e)

                }
                is JSONException -> {
                    ex = AppException(Error.PARSE_ERROR, e)
                }
                is AppException -> {
                    return it
                }

                else -> {
                    ex = AppException(Error.UNKNOWN, e)
                }
            }
        }
        (ex.errorMsg + "\n" + ex.errorLog).logE()
        return ex
    }
}