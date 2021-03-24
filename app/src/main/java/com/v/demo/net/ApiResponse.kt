package com.v.demo.net

import com.v.base.net.BaseResponse


class ApiResponse<T>(var status: Int, var msg: String, var data: T)  : BaseResponse<T>() {

    // 这里是示例，wanandroid 网站返回的 错误码为 0 就代表请求成功，请你根据自己的业务需求来编写
    override fun isSuccess() = status == 100

    override fun getResponseCode() = status

    override fun getResponseData() = data!!

    override fun getResponseMsg() = msg

}