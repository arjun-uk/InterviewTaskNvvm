package com.education.interviewtask.presentation.adapter

import android.content.Context
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.education.interviewtask.data.constants.Constants
import com.education.interviewtask.data.model.product.Product
import com.education.interviewtask.databinding.ItemProductsSingleBinding

class AdapterProducts(private val context: Context, private var productsList: List<Product>,private val handler: Handler) : RecyclerView.Adapter<AdapterProducts.MyViewHolder>() {
    private lateinit var binding: ItemProductsSingleBinding
    fun setProductsList(productsList: List<Product>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        binding = ItemProductsSingleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        binding.txtProductTitle.text = productsList[position].title
        binding.txtProductsCategory.text = productsList[position].category
        binding.txtProductsPrice.text = String.format("%S%s", Constants.RUPEE_SYMBOL, productsList[position].price.toString())
        Glide.with(context).load(productsList[position].thumbnail).into(binding.imgProduct)
        binding.root.setOnClickListener {
            sentMessageThroughHandler(100,productsList[position])
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
    private fun sentMessageThroughHandler(id: Int, obj:Any?){
        val msg = Message()
        msg.what = id
        msg.obj = obj
        handler.sendMessage(msg)
    }

    class MyViewHolder(binding: ItemProductsSingleBinding) : RecyclerView.ViewHolder(binding.root)


}
