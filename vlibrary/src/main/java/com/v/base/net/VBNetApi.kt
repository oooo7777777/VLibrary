package com.v.base.net

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface VBNetApi {


    @GET
    suspend fun get(@Url url: String): String

    @GET
    suspend fun get(@Url url: String, @QueryMap map: Map<String, @JvmSuppressWildcards Any>): String

    @POST
    suspend fun post(@Url url: String): String

    @POST
    suspend fun post(@Url url: String, @Body body: RequestBody): String

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