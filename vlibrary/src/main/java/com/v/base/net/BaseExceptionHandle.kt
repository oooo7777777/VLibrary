package com.v.base.net

import com.v.base.annotaion.Error
import com.v.base.utils.ext.logE
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException


/**
 * author  : ww
 * desc    : 错误处理
 * time    : 2021-03-16 09:52:45
 */
object BaseExceptionHandle {

    fun handleException(e: Throwable): BaseAppException {
        val ex: BaseAppException
        e.let {
            when (it) {
                is BaseAppException -> {
                    ex = it
                }
                is HttpException -> {
                    ex = BaseAppException(Error.NETWORK_ERROR, e)
                }
                is ConnectException -> {
                    ex = BaseAppException(Error.NETWORK_ERROR, e)
                }
                is javax.net.ssl.SSLException -> {
                    ex = BaseAppException(Error.SSL_ERROR, e)
                }
                is ConnectTimeoutException -> {
                    ex = BaseAppException(Error.TIMEOUT_ERROR, e)
                }
                is java.net.SocketTimeoutException -> {
                    ex = BaseAppException(Error.TIMEOUT_ERROR, e)
                }
                is java.net.UnknownHostException -> {
                    ex = BaseAppException(Error.TIMEOUT_ERROR, e)
                }
                is JSONException -> {
                    ex = BaseAppException(Error.PARSE_ERROR, e)
                }
                else -> {
                    ex = BaseAppException(Error.UNKNOWN, e)
                }
            }
        }
        (ex.toString()).logE()
        return ex
    }
}