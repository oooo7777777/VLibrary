package com.v.base.net


/**
 * author  : ww
 * desc    :
 * time    : 2021-03-16 09:52:45
 */
abstract class BaseResponse<T> {

    abstract fun isSuccess(): Boolean

    abstract fun getResponseData(): T

    abstract fun getResponseCode(): Int

    abstract fun getResponseMsg(): String

}