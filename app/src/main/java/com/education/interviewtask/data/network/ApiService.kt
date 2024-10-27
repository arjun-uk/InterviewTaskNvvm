package com.education.interviewtask.data.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products")
    suspend fun getProducts(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): Response<ResponseBody>

    @GET("products/{id}")
    suspend fun getProductDetail(
        @Path("id") id: Int
    ): Response<ResponseBody>

}