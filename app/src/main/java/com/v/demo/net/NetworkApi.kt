package com.v.demo.net

import com.v.demo.bean.BannerBean
import com.v.demo.bean.GirlBean
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * @Author : ww
 * desc    :
 * time    : 2021/3/20 11:05
 */
interface NetworkApi {

    companion object {
        const val SERVER_URL = "https://gank.io/api/v2/"
    }

    @GET("banners")
    suspend fun getBanner(): ApiResponse<ArrayList<BannerBean>>


    @GET("data/category/Girl/type/Girl/page/{page}/count/20")
    suspend fun getGirlBean(@Path("page") page: Int): ApiResponse<ArrayList<GirlBean>>

}