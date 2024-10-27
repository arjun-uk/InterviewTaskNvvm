package com.education.interviewtask.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.education.interviewtask.data.respository.ProductRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

class ProductViewModel :ViewModel() {
    private val repository = ProductRepository()

    private val _products = MutableLiveData<JSONObject>()
    val products: LiveData<JSONObject> get() = _products
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error


    fun fetchProducts(limit: Int, skip: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getProducts(limit, skip)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val jsonObject = responseBody?.let { JSONObject(it) }
                    _products.postValue(jsonObject)
                } else {
                    _error.postValue("Failed to fetch products")
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
    fun getProductDetail(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getProductDetail(id)
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string()
                    val jsonObject = responseBody?.let { JSONObject(it) }
                    _products.postValue(jsonObject)
                } else {
                    _error.postValue("Failed to fetch product detail")
                }
            } catch (e: Exception) {
                _error.postValue(e.message)
            }

        }
    }
}