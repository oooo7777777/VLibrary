package com.v.base.net

import com.alibaba.fastjson.JSON
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type
import java.nio.charset.Charset

class FastJsonConverterFactory(val charset: Charset) : Converter.Factory()
{


    override fun requestBodyConverter(
        type: Type?,
        parameterAnnotations: Array<Annotation>?,
        methodAnnotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<*, RequestBody>?
    {
        return JsonRequestBodyConverter<String>() //请求
    }

    override fun responseBodyConverter(
        type: Type?,
        annotations: Array<Annotation>?,
        retrofit: Retrofit?
    ): Converter<ResponseBody, *>?
    {
        return JsonResponseBodyConverter<String>(type!!) //响应
    }

    private inner class JsonRequestBodyConverter<T> : Converter<T, RequestBody>
    {
        private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")

        @Throws(IOException::class)
        override fun convert(value: T): RequestBody
        {
            return RequestBody.create(MEDIA_TYPE, value.toString())
        }
    }

    private inner class JsonResponseBodyConverter<T>(private val type: Type) : Converter<ResponseBody, T>
    {

        @Throws(IOException::class)
        override fun convert(value: ResponseBody): T?
        {
            val bytes = value.bytes()
            try
            {
                val result = String(bytes)
                return JSON.parseObject<T>(result, type)
            } finally
            {
                value.close()
            }

        }


    }

    companion object
    {
        private val UTF_8 = Charset.forName("UTF-8")

        @JvmOverloads
        fun create(charset: Charset = UTF_8): FastJsonConverterFactory
        {
            return FastJsonConverterFactory(charset)
        }
    }
}
