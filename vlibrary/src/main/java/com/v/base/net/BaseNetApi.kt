package com.v.base.net


import retrofit2.http.*


interface BaseNetApi {

    @GET
    suspend fun get(@Url Path: String): Any

    @GET
    suspend fun get(@Url Path: String, @QueryMap map: Map<String, @JvmSuppressWildcards Any>): Any


    @FormUrlEncoded
    @POST
    suspend fun post(@Url Path: String): Any

    @FormUrlEncoded
    @POST
    suspend fun post(@Url Path: String, @FieldMap map: Map<String, @JvmSuppressWildcards Any>): Any


}