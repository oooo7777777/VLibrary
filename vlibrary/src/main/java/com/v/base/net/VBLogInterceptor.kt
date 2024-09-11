package com.v.base.net

import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.v.log.util.logD
import com.v.log.util.logE
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import java.io.EOFException
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.util.concurrent.TimeUnit


/**
 * author  : ww
 * desc    : 网络请求日志打印
 * time    : 2021-03-16 09:52:45
 */
open class VBLogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return requestBodyFormat(chain)
    }

    @Throws(IOException::class)
    fun requestBodyFormat(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val sb = StringBuffer()
        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val connection = chain.connection()
        val protocol = if (connection != null) connection.protocol() else Protocol.HTTP_1_1
        var requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol
        if (hasRequestBody) {
            requestStartMessage += " (" + requestBody!!.contentLength() + "-byte body)"
        }
        sb.append(requestStartMessage)


        if (!hasRequestBody) {
            sb.append("\n")
            sb.append("--> END " + request.method())
        } else if (bodyEncoded(request.headers())) {
            sb.append("\n")
            sb.append("--> END " + request.method() + " (encoded body omitted)")
        } else {
            sb.append("--> END ")
            sb.append("\n")
            sb.append(
                request.method() + " (binary "
                        + requestBody!!.contentLength() + "-byte body omitted)"
            )

        }

        val startNs = System.nanoTime()
        val response = try {
            chain.proceed(request)
        } catch (e: java.lang.Exception) {
            sb.append("\n")
            sb.append("<-- HTTP FAILED: $e")
            sb.append("\n")
            sb.append(Log.getStackTraceString(e))
            sb.logE("VBLogInterceptor")
            throw e
        }
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body()
        val contentLength = responseBody!!.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        sb.append("\n")
        sb.append(
            "<-- " + response.code() + ' ' + response.message() + ' '
                    + response.request().url() + " (" + tookMs + "ms " +
                    bodySize + " body" + ')'
        )
        sb.append("\n\n")


        //请求头
        request.headers().apply {
            if (this.size() > 0) {
                sb.append("Request Headers:  ")
                    .append(" {")
                for (i in 0 until this.size()) {
                    sb.append(this.name(i) + "=" + this.value(i))
                        .append(", ")
                }
                sb.append("}")
                    .append("\n--> END ")
                    .append("\n\n")
            }
        }


        //获取请求体
        if (hasRequestBody) {
            val buffer = Buffer()
            requestBody!!.writeTo(buffer)
            var parameter = ""
            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            parameter = buffer.readString(charset!!)
            sb.append("Request Parameter: ${parameter}")
                .append("\n")
            sb.append("--> END ")
                .append(" (")
                .append(requestBody.contentLength())
                .append("-byte body)")
                .append("\n\n")
        }


        //返回的的请求头
        response.headers().apply {
            if (this.size() > 0) {
                sb.append("Response Headers: {")
                for (i in 0 until this.size()) {
                    sb.append(this.name(i) + "=" + this.value(i))
                        .append(", ")
                }
                sb.append("}")
                    .append("\n--> END ")
                    .append("\n\n")
            }
        }

        if (!HttpHeaders.hasBody(response)) {
            sb.append("<-- END HTTP")
        } else if (bodyEncoded(response.headers())) {
            sb.append("<-- END HTTP (encoded body omitted)")
        } else {
            val source = responseBody!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer()
            var charset: Charset = UTF8
            val contentType = responseBody!!.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8)!!
                } catch (e: UnsupportedCharsetException) {
                    sb.append("\n")
                    sb.append("")
                    sb.append("Couldn't decode the response body; charset is likely malformed.")
                    sb.append("<-- END HTTP")
                    sb.logE("VBLogInterceptor",save = false)
                    return response
                }
            }
            if (!isPlaintext(buffer)) {
                sb.append("\n")
                sb.append("")
                sb.append("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)")
                sb.logD(save = false)
                return response
            }
            if (contentLength != 0L) {
                sb.append("")
                val json = buffer.clone().readString(charset)
                sb.append(json)
                sb.append("\n\n")
                sb.append(prettyJson(json))
            }
            sb.append("\n\n")
            sb.append("<-- END HTTP (" + buffer.size() + "-byte body)")
        }

        (sb.toString().replace(", }", "}")).logD(request.url().encodedPath(), save = false)
        return response
    }


    /**
     * json 美化
     * @param json
     * @return
     */
    private fun prettyJson(json: String): String {
        val json = json
        if (TextUtils.isEmpty(json)) {
            return "Empty/Null json content"
        }
        var jsonObject: JSONObject? = null
        try {
            jsonObject = JSONObject.parseObject(json)
        } catch (e: Exception) {
            return json
        }
        return JSONObject.toJSONString(jsonObject, true)
    }


    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }


    companion object {
        private val UTF8 = Charset.forName("UTF-8")

        /**
         * 判断是否为明文
         *
         * @param buffer
         * @return
         */
        fun isPlaintext(buffer: Buffer): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = if (buffer.size() < 64) buffer.size() else 64
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                return true
            } catch (e: EOFException) {
                return false
            }

        }
    }

}