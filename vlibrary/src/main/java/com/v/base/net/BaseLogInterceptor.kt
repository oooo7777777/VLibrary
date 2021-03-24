package com.v.base.net

import android.text.TextUtils
import com.v.base.utils.logD
import com.v.base.utils.logE
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.http.HttpHeaders
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
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
open class BaseLogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return requestBodyFormat(chain)
    }

    @Throws(IOException::class)
    fun requestBodyFormat(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body()
        val hasRequestBody = requestBody != null

        val sb = StringBuilder()
        sb.append("--> ")
            .append(request.method())
            .append(" ")
            .append(request.url())
        if (hasRequestBody) {
            sb.append(" (")
                .append(requestBody!!.contentLength())
                .append("-byte body)")
        }

        sb.append("\n")

        if (hasRequestBody) {
            if (requestBody!!.contentType() != null) {
                sb.append("Content-Type: ")
                    .append(requestBody.contentType())
                    .append("\n")
            }
            if (requestBody.contentLength() != -1L) {
                sb.append("Content-Length: ")
                    .append(requestBody.contentLength())
                    .append("\n")
            }
        }

        val headers = request.headers()
        run {
            var i = 0
            val count = headers.size()
            while (i < count) {
                val name = headers.name(i)
                if (!"Content-Type".equals(name, ignoreCase = true) && !"Content-Length".equals(
                        name,
                        ignoreCase = true
                    )
                ) {
                    sb.append(name)
                        .append(": ")
                        .append(headers.value(i))
                        .append("\n")
                }
                i++
            }
        }

        if (!hasRequestBody) {
            sb.append("--> END ")
                .append(request.method())
                .append("\n")
        } else {
            //这里获取请求体
            val buffer = Buffer()
            requestBody!!.writeTo(buffer)
            var parameter = ""
            var charset = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            parameter = buffer.readString(charset!!)
            sb.append("Required Parameter: ${parameter}")
                .append("\n")

            sb.append("--> END ")
                .append(request.method())
                .append(" (")
                .append(requestBody.contentLength())
                .append("-byte body)")
                .append("\n")
        }

        // 这里获取响应体
        val startNs = System.nanoTime()
        val response = chain.proceed(request)
        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        val responseBody = response.body()

        if (responseBody != null) {
            val contentLength = responseBody.contentLength()
            val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
            sb.append("<-- ")
                .append(response.code())
                .append(" ")
                .append(response.message())
                .append(" ")
                .append(response.request().url())
                .append(" (")
                .append(tookMs)
                .append("ms, ")
                .append(bodySize)
                .append(" body)")
                .append("\n")

            val headers1 = response.headers()
            var i = 0
            val count = headers1.size()
            while (i < count) {
                sb.append(headers1.name(i))
                    .append(": ")
                    .append(headers1.value(i))
                    .append("\n")
                i++
            }

            if (!HttpHeaders.hasBody(response)) {
                sb.append("<-- END HTTP")
                    .append("\n")
            } else if (bodyEncoded(response.headers())) {
                sb.append("<-- END HTTP (encoded body omitted)")
                    .append("\n")
            } else {

                val source = responseBody.source()
                source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
                val buffer = source.buffer()

                var charset = UTF8
                val contentType = responseBody.contentType()

                if (contentType != null) {
                    try {
                        charset = contentType.charset(UTF8)
                    } catch (e: UnsupportedCharsetException) {
                        sb.append("\n")
                        sb.append("Couldn't decode the response body; charset is likely malformed.")
                            .append("\n")
                        sb.append("<-- END HTTP")
                            .append("\n")

                        return response
                    }

                }

                if (isPlaintext(buffer)) {
                    if (contentLength != 0L) {
                        sb.append("\n")
                        if (charset != null) {

                            var json = ""
                            json = buffer.clone()
                                .readString(charset)
                            sb.append(json)
                                .append("\n\n")
                            val str = jsonFormat(json)
                            sb.append(str)
                                .append("\n")
                        }
                    }
                }

                sb.append("<-- END HTTP (")
                    .append(buffer.size())
                    .append("-byte body)")
                    .append("\n")
            }
        }
        sb.toString().logD()
        return response

    }


    /**
     * 将json字符串格式化后返回
     *
     * @param json json字符串
     * @return 格式化后的字符串
     */
    private fun jsonFormat(json: String): String {
        var json = json
        if (TextUtils.isEmpty(json)) {
            return "Empty/Null json content"
        }
        try {
            json = json.trim { it <= ' ' }
            val message: String
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                message = jsonObject.toString(2)
                return message
            } else if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                message = jsonArray.toString(2)
                return message
            } else {
                message = "Invalid Json"
            }
            return message
        } catch (e: JSONException) {
            e.message.toString().logE()
            return "Invalid Json"
        }

    }


    private fun bodyEncoded(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return contentEncoding != null && !contentEncoding.equals("identity", ignoreCase = true)
    }

    /**
     * 是否打印返回数据
     *
     * 打印返回数据会造成多次请求，部分接口不能多次请求
     *
     * @param url 请求url
     * @return 是否返回
     */
    private fun showResponse(url: String): Boolean {
        var showResponse = true
        if (false) {
            showResponse = false
        }
        return showResponse
    }

    companion object {
        val UTF8 = Charset.forName("UTF-8")


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