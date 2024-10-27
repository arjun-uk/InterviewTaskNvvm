package com.education.interviewtask.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.education.interviewtask.R
import com.education.interviewtask.data.constants.Constants
import com.education.interviewtask.data.model.product.Product
import com.education.interviewtask.data.viewmodel.ProductViewModel
import com.education.interviewtask.databinding.ActivityHomeBinding
import com.education.interviewtask.presentation.adapter.AdapterProducts
import org.json.JSONArray


class ActivityHome : SuperActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var productViewModel: ProductViewModel
    private lateinit var adapterProducts: AdapterProducts
    private var page = 0
    private var isLoading = false
    private var hasMoreData = true
    private val productsList = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initViews()
        setupObservers()
    }

    private fun initViews() {
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        adapterProducts = AdapterProducts(this, productsList, handler)
        binding.ProductsList.apply {
            layoutManager = LinearLayoutManager(this@ActivityHome, LinearLayoutManager.VERTICAL, false)
            adapter = adapterProducts
            addOnScrollListener(mListener)
            visibility = View.GONE
        }
        loadProducts()
    }

    private fun setupObservers() {
        productViewModel.products.observe(this) { jsonObjects ->
            binding.ProductsList.visibility = View.VISIBLE
            hideLoadingDialog()
            binding.loading.visibility = View.GONE
            isLoading = false

            val productsArray = jsonObjects.getJSONArray("products")
            // Check if we've reached the end of available data
            if (productsArray.length() < Constants.PAGE_LIMIT) {
                hasMoreData = false
            }
            processProducts(productsArray)
            page++
        }

        productViewModel.error.observe(this) { errorMessage ->
            hideLoadingDialog()
            binding.loading.visibility = View.GONE
            isLoading = false
            showToast(errorMessage)
        }
    }

    private fun loadProducts() {
        if (!isNetworkAvailable(this)) {
            showToast("No Internet Connection")
            return
        }

        if (isLoading || !hasMoreData) {
            return
        }

        showLoadingDialog()
        binding.loading.visibility = View.VISIBLE
        isLoading = true
        productViewModel.fetchProducts(Constants.PAGE_LIMIT, page * Constants.PAGE_LIMIT)
    }

    private fun processProducts(productsArray: JSONArray) {
        try {
            val newProducts = ArrayList<Product>()
            for (i in 0 until productsArray.length()) {
                val productObject = productsArray.getJSONObject(i)
                val product = Product(
                    id = productObject.getInt("id"),
                    title = productObject.getString("title"),
                    description = productObject.getString("description"),
                    price = productObject.getDouble("price"),
                    discountPercentage = productObject.getDouble("discountPercentage"),
                    rating = productObject.getDouble("rating"),
                    stock = productObject.getInt("stock"),
                    brand = "",
                    category = productObject.getString("category"),
                    thumbnail = productObject.getString("thumbnail"),
                    images = ArrayList<String>().apply {
                        val images = productObject.getJSONArray("images")
                        for (j in 0 until images.length()) {
                            add(images.getString(j))
                        }
                    }
                )
                newProducts.add(product)
            }

            // Append new products to existing list
            productsList.addAll(newProducts)
            adapterProducts.notifyItemRangeInserted(productsList.size - newProducts.size, newProducts.size)
        } catch (e: Exception) {
            Log.e("HomeActivity", "Error processing products: ${e.message}", e)
        }
    }

    private val handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            100 -> {
                val product = msg.obj as Product
                val intent = Intent(this, ActivityProductDetail::class.java)
                intent.putExtra("id", product.id)
                startActivity(intent)
            }
        }
        true
    }

    private val mListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (!recyclerView.canScrollVertically(1) &&
                newState == RecyclerView.SCROLL_STATE_IDLE &&
                !isLoading &&
                hasMoreData
            ) {
                loadProducts()
            }
        }
    }
}