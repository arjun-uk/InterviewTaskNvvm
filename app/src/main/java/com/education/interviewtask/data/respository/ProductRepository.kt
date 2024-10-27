package com.education.interviewtask.data.respository

import com.education.interviewtask.data.network.RetrofitInstance

class ProductRepository {
    private val apiService = RetrofitInstance.apiService
    suspend fun getProducts(limit: Int, skip: Int) = apiService.getProducts(limit, skip)
    suspend fun getProductDetail(id: Int) = apiService.getProductDetail(id)
}