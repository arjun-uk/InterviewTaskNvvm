package com.education.interviewtask.presentation.adapter

import android.content.Context
import android.os.Handler
import android.os.Message
import android.provider.MediaStore.Images
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.education.interviewtask.data.model.product.Product
import com.education.interviewtask.databinding.ItemImageSingleBinding
import com.education.interviewtask.databinding.ItemProductsSingleBinding

class AdapterImages(private val context: Context, private var productsList: List<String>) : RecyclerView.Adapter<AdapterImages.MyViewHolder>() {
    private lateinit var binding: ItemImageSingleBinding
    fun setProductsList(productsList: List<String>) {
        this.productsList = productsList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterImages.MyViewHolder {
        binding = ItemImageSingleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdapterImages.MyViewHolder, position: Int) {
        Glide.with(context).load(productsList[position]).into(binding.image)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    class MyViewHolder(binding: ItemImageSingleBinding) : RecyclerView.ViewHolder(binding.root)


}
