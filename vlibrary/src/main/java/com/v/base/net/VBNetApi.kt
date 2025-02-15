package com.v.base.net

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.QueryMap
import retrofit2.http.Streaming
import retrofit2.http.Url

interface VBNetApi {


    @GET
    suspend fun get(@Url url: String): String

    @GET
    suspend fun get(@Url url: String, @QueryMap map: Map<String, @JvmSuppressWildcards Any>): String

    @POST
    suspend fun post(@Url url: String): String

    @POST
    suspend fun post(@Url url: String, @Body body: RequestBody): String

    @FormUrlEncoded
    @POST
    suspend fun post(@Url url: String, @FieldMap map: Map<String, @JvmSuppressWildcards Any>): Any

    @PUT
    suspend fun put(@Url url: String, @Body body: RequestBody): String

    @DELETE
    suspend fun delete(
        @Url url: String,
        @QueryMap map: Map<String, @JvmSuppressWildcards Any>,
    ): String

    @Multipart
    @POST
    suspend fun uploadFile(@Url url: String, @Part file: MultipartBody.Part): String


    @Streaming
    @GET
    fun download(@Url url: String): Call<ResponseBody>


}