package com.v.base.net


import retrofit2.http.*


interface NetApi {
    @GET
    suspend fun get(@Url Path: String): ApiResponse<Any>

    @GET
    suspend fun get(@Url Path: String, @QueryMap map: Map<String, @JvmSuppressWildcards Any>): ApiResponse<Any>


    @FormUrlEncoded
    @POST
    suspend fun post(@Url Path: String): ApiResponse<Any>

    @FormUrlEncoded
    @POST
    suspend fun post(@Url Path: String, @FieldMap map: Map<String, @JvmSuppressWildcards Any>): ApiResponse<Any>


    @GET
    suspend fun getDefault(@Url Path: String): Any

    @GET
    suspend fun getDefault(@Url Path: String, @QueryMap map: Map<String, @JvmSuppressWildcards Any>): Any


    @FormUrlEncoded
    @POST
    suspend fun postDefault(@Url Path: String): Any

    @FormUrlEncoded
    @POST
    suspend fun postDefault(@Url Path: String, @FieldMap map: Map<String, @JvmSuppressWildcards Any>): Any



}