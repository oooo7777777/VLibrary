package com.v.demo.net

import com.v.demo.bean.BannerBean
import com.v.demo.bean.HomeBean
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * @Author : ww
 * desc    : 自定义的Api
 * time    : 2021/3/20 11:05
 */
interface NetworkApi {

    companion object {
        const val SERVER_URL = "https://www.wanandroid.com/"
    }


    @GET("banner/json")
    suspend fun getBannerBean(): BannerBean

}