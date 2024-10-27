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
    }

    private fun initViews(){
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        adapterProducts = AdapterProducts(this, emptyList(),handler)
        binding.ProductsList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.ProductsList.adapter = adapterProducts
        binding.ProductsList.addOnScrollListener(mListener)
        binding.ProductsList.visibility = View.GONE
        loadProducts(0)
    }

    private fun loadProducts(skip: Int) {
        if (isNetworkAvailable(this)){
            showLoadingDialog()
            binding.loading.visibility = View.VISIBLE
            isLoading = true
            productViewModel.fetchProducts(Constants.PAGE_LIMIT, skip)
        }else{
            showToast("No Internet Connection")
        }

        productViewModel.products.observe(this) {jsonObjects ->
            binding.ProductsList.visibility = View.VISIBLE
            hideLoadingDialog()
            binding.loading.visibility = View.GONE
            isLoading = false
            val productsArray = jsonObjects.getJSONArray("products")
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
    private fun processProducts(productsArray: JSONArray) {
        try {
            val list = ArrayList<Product>()
            for (i in 0 until productsArray.length()) {
                val productObject = productsArray.getJSONObject(i)
                val id = productObject.getInt("id")
                val title = productObject.getString("title")
                val description = productObject.getString("description")
                val price = productObject.getDouble("price")
                val discountPercentage = productObject.getDouble("discountPercentage")
                val rating = productObject.getDouble("rating")
                val stock = productObject.getInt("stock")
                val brand = productObject.getString("brand")
                val category = productObject.getString("category")
                val thumbnail = productObject.getString("thumbnail")
                val images = productObject.getJSONArray("images")
                val imageList = ArrayList<String>()
                for (j in 0 until images.length()) {
                    imageList.add(images.getString(j))
                }
                val productsList = Product(id, title, description, price, discountPercentage, rating, stock, brand, category, thumbnail, imageList)
                list.add(productsList)
                adapterProducts.setProductsList(list)
            }
        } catch (e: Exception) {
            Log.e("HomeActivity", e.message.toString() )
        }

    }
    private val handler = Handler(Looper.getMainLooper()){msg->
        when(msg.what){
            100 ->{
                val product = msg.obj as Product
                val intent = Intent(this, ActivityProductDetail::class.java)
                intent.putExtra("id", product.id)
                startActivity(intent)
            }
        }
        true
    }
    private val mListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    loadProducts(page * Constants.PAGE_LIMIT)
                }
            }
        }

}