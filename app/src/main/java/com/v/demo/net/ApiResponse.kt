package com.v.demo.net

import com.v.base.bean.VBResponse

class ApiResponse<T> : VBResponse<T>() {

    var errorCode = 0
    var errorMsg = ""
    var data: T? = null

    // 这里是示例，wanandroid 网站返回的 错误码为 0 就代表请求成功，请你根据自己的业务需求来编写
    override fun isSuccess() = errorCode == 0

    override fun getResponseCode() = errorCode

    override fun getResponseData() = data!!

    override fun getResponseMsg() = errorMsg

}