package com.v.demo.net

import com.v.demo.bean.BannerBean
import com.v.demo.bean.HomeBean
import retrofit2.http.GET
import retrofit2.http.Headers
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


    @Headers("Authorization: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJuaWNrbmFtZSI6IueOi-WTqui3kSIsImlkIjoiMTQ3NjQ2NDAxMDA2ODg2NTAyNiIsImF2YXRhciI6Imh0dHBzOi8vc3RhdGljLm1lcmFjaC5jb20vYXZhdGFyLzIwMjIwMTEyL3NjVnVmNWFmQTE2Y01QZUguanBnIiwidHlwZSI6MSwibG9naW4iOjEsImV4cCI6MTY0MzQyMTExNSwidXVpZCI6ImFkMzM0ZmZhMjI5YzQ4OTdiODM5OWJlOGIzZmQ1NjNiIn0.paG0msb3VKDzpCdLNzHFsmxavLvGmIO3SvRs09Z7VFm4fwdx2heGtNrHmDInvdzXkitY70aXtkbty-2ATGa07w",
    "X-LANGUAGE-KEY: zh_CN",
    "User-Agent: 9999")
    @GET("https://testapi.merach.com/course/detail?courseId=1464417681831038978")
    suspend fun getGirlBean(): String

}