package com.v.base.net

import android.text.TextUtils
import com.alibaba.fastjson.JSONObject
import com.v.base.utils.logD
import com.v.base.utils.logE
import okhttp3.Headers
import okhttp3.Interceptor
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
open class VBLogInterceptor(var logTag: String = "PRETTY_LOGGER") : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        return requestBodyFormat(chain)
    }

    @Throws(IOException::class)
    fun requestBodyFormat(chain: Interceptor.Chain): Response {
        val sb = StringBuilder()
        var isException = false
        try {
            val request = chain.request()
            val requestBody = request.body()
            val hasRequestBody = requestBody != null

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
                                val str = prettyJson(json)
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

        } catch (e: Exception) {
            isException = true
            sb.append("EXCEPTION:$e")
            e.printStackTrace()
        } finally {
            if (isException) {
                sb.toString().logE(logTag)
            } else {
                sb.toString().logD(logTag)
            }
            return chain.proceed(chain.request())
        }
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