package com.ww.appmodule.net

import com.v.base.net.BaseResponse


class ApiResponse<T> : BaseResponse<T>() {

    var status = 0
    var msg = ""
    var data: T? = null

    override fun isSuccess() = status == 100

    override fun getResponseCode() = status

    override fun getResponseData() = data!!

    override fun getResponseMsg() = msg

}