package com.ww.appmodule.net

import android.text.TextUtils
import com.v.base.BaseApplication
import com.v.base.utils.ext.getAppVersionCode
import com.ww.appmodule.UserUtil
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


class RequestHeaderInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val original: Request = chain.request()
        //获取当前时间戳
        val time = System.currentTimeMillis().toString()
        //验签 请求参数集合有序
        var bodyMap = TreeMap<String, String>()
        //判断是否是表单提交
        val method = original.method()
        val sb = StringBuilder()
        if ("POST" == method) {
            if (original.body() is FormBody) {
                val body = original.body() as FormBody?
                for (i in 0 until body!!.size()) {
                    if (!TextUtils.isEmpty(body.encodedValue(i))) {
                        bodyMap[body.encodedName(i)] = body.encodedValue(i)
                    }
                }
            }
        } else {
            bodyMap = urlRequest(original.url().url().toString())
        }
        bodyMap["X-Cusmall-Timestamp"] = time
        for ((key, value) in bodyMap) {
            sb.append(key)
                .append("=")
                .append(value)
                .append(",")
        }
        sb.delete(sb.length - 1, sb.length)

        //MD5加密 验签字符串
        val sign = stringToMD5(sb.toString() + "mkd-api-sign-key")
        val requestBuilder = original.newBuilder()
            .addHeader("Content-Type", "application/json;charset=UTF-8")
            .addHeader("X-Cusmall-Token", UserUtil.getToken())
            .addHeader("X-Cusmall-Version", BaseApplication.getContext().getAppVersionCode().toString())
            .addHeader("X-Cusmall-Timestamp", time + "")
            .addHeader("X-Cusmall-Sign", sign)
            .addHeader(
                "X-Cusmall-Origin", "10"
            ) //猩家来源 IOS 传9, 安卓传10	1:天淘微信小程序，2:天淘支付宝小程序，3:天淘ios , 4:天淘Android ,5:神鸟微信小程序 , 6:神鸟支付宝小程序,7:神鸟IOS,8:神鸟Android


        val request = requestBuilder.build()
        return chain.proceed(request)

    }

    private fun urlRequest(URL: String): TreeMap<String, String> {
        val mapRequest = TreeMap<String, String>()
        var arrSplit: Array<String>? = null
        val strUrlParam: String = truncateUrlPage(URL)
            ?: return mapRequest
        arrSplit = strUrlParam.split("[&]".toRegex()).toTypedArray()
        for (strSplit in arrSplit) {
            var arrSplitEqual: Array<String>? = null
            arrSplitEqual = strSplit.split("[=]".toRegex()).toTypedArray()

            //解析出键值
            if (arrSplitEqual.size > 1) {
                //正确解析
                if (!TextUtils.isEmpty(arrSplitEqual[1])) {
                    mapRequest[arrSplitEqual[0]] = arrSplitEqual[1]
                }
            }
        }
        return mapRequest
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     *
     * @param strURL url地址
     * @return url请求参数部分
     */
    private fun truncateUrlPage(strURL: String): String? {
        var strURL = strURL
        var strAllParam: String? = null
        var arrSplit: Array<String?>? = null
        strURL = strURL.trim { it <= ' ' }
        arrSplit = strURL.split("[?]".toRegex()).toTypedArray()
        if (strURL.length > 1) {
            if (arrSplit.size > 1) {
                if (arrSplit[1] != null) {
                    strAllParam = arrSplit[1]
                }
            }
        }
        return strAllParam
    }

    /**
     * 将字符串转成MD5值
     *
     * @param string
     * @return
     */
    private fun stringToMD5(string: String): String? {
        try {
            val bmd5 = MessageDigest.getInstance("MD5")
            bmd5.update(string.toByteArray())
            var i: Int
            val buf = StringBuffer()
            val b = bmd5.digest()
            for (offset in b.indices) {
                i = b[offset].toInt()
                if (i < 0) {
                    i += 256
                }
                if (i < 16) {
                    buf.append("0")
                }
                buf.append(Integer.toHexString(i))
            }
            return buf.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

}