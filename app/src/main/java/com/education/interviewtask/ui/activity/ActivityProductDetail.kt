package com.education.interviewtask.ui.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.education.interviewtask.R
import com.education.interviewtask.data.constants.Constants
import com.education.interviewtask.data.model.product.Reviews
import com.education.interviewtask.data.viewmodel.ProductViewModel
import com.education.interviewtask.databinding.ActivityProductDetailBinding
import com.education.interviewtask.presentation.adapter.AdapterImages
import com.education.interviewtask.presentation.adapter.AdapterReviews
import com.education.interviewtask.ui.utils.AppUtils
import org.json.JSONArray
import org.json.JSONObject

class ActivityProductDetail : SuperActivity() {
    private lateinit var productViewModel: ProductViewModel
    private lateinit var binding: ActivityProductDetailBinding
    private lateinit var adapterReviews: AdapterReviews
    private lateinit var adapterImages: AdapterImages
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
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
        adapterReviews = AdapterReviews( emptyList())
        binding.reviewsList.layoutManager = LinearLayoutManager(this)
        binding.reviewsList.setHasFixedSize(true)
        binding.reviewsList.adapter = adapterReviews
        adapterImages = AdapterImages(this, emptyList())
        binding.imageList.layoutManager = GridLayoutManager(this, 3)
        binding.imageList.setHasFixedSize(true)
        binding.imageList.adapter = adapterImages
        val id = intent.getIntExtra("id", 0)
        binding.backBtn.setOnClickListener {
            finish()
        }
        loadProductDetail(id)
    }

    private fun loadProductDetail(id: Int) {
        if (isNetworkAvailable(this)){
            showLoadingDialog()
            productViewModel.getProductDetail(id)
        }else{
            showToast("No Internet Connection")
        }
        productViewModel.products.observe(this) {jsonObjects ->
            hideLoadingDialog()
            processProductsDetails(jsonObjects)
            processProductReviews(jsonObjects.getJSONArray("reviews"))
        }
    }

    private fun processProductsDetails(jsonObjects: JSONObject){
        val title = AppUtils.getStringValueFromJson(jsonObjects,"title")
        val description = AppUtils.getStringValueFromJson(jsonObjects,"description")
        val price = AppUtils.getDoubleValueFromJson(jsonObjects,"price")
        val rating = AppUtils.getFloatValueFromJson(jsonObjects,"rating")
        val stock = AppUtils.getIntValueFromJson(jsonObjects,"stock")
        val category = AppUtils.getStringValueFromJson(jsonObjects,"category")
        val thumbnail = AppUtils.getStringValueFromJson(jsonObjects,"thumbnail")
        val images = jsonObjects.getJSONArray("images")
        val imageList = ArrayList<String>()
        for (j in 0 until images.length()) {
            imageList.add(images.getString(j))
            adapterImages.setProductsList(imageList)
        }
        binding.title.text = title
        binding.description.text = description
        binding.rating.rating = rating
        binding.stock.text = String.format("%S%s", "stock :", stock)
        binding.price.text = String.format("%S%s", Constants.RUPEE_SYMBOL, price)
        binding.category.text = category
        Glide.with(this).load(thumbnail).into(binding.image)
    }

    private fun processProductReviews(jsonObjects: JSONArray){
        val list = ArrayList<Reviews>()
        for (i in 0 until jsonObjects.length()) {
            val productObject = jsonObjects.getJSONObject(i)
            val date = AppUtils.getStringValueFromJson(productObject,"date")
            val rating = AppUtils.getFloatValueFromJson(productObject,"rating")
            val comment = AppUtils.getStringValueFromJson(productObject,"comment")
            val reviewerEmail = AppUtils.getStringValueFromJson(productObject,"reviewerEmail")
            val reviewerName = AppUtils.getStringValueFromJson(productObject,"reviewerName")
            val productsList = Reviews(date, rating, comment, reviewerEmail, reviewerName)
            list.add(productsList)
            adapterReviews.setProductsList(list)
        }
    }
}
