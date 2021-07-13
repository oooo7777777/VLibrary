package com.v.base.bean


class VBApiResponse<T> : VBResponse<T>() {

    var status = 0
    var msg = ""
    var data: T? = null

    override fun isSuccess() = status == 100

    override fun getResponseCode() = status

    override fun getResponseData() = data!!

    override fun getResponseMsg() = msg

}