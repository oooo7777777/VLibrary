package com.v.demo.net

import com.v.demo.bean.BannerBean
import com.v.demo.bean.HomeBean
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @Author : ww
 * desc    :
 * time    : 2021/3/20 11:05
 */
interface NetworkApi {

    companion object {
        const val SERVER_URL = "https://www.wanandroid.com/"
    }

    @GET("banner/json")
    suspend fun getBanner(): ApiResponse<ArrayList<BannerBean>>


    @GET("article/list/{page}/json")
    suspend fun getGirlBean(@Path("page") page: Int): ApiResponse<HomeBean>

}