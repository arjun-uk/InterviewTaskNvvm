package com.education.interviewtask.presentation.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.education.interviewtask.data.model.product.Reviews
import com.education.interviewtask.databinding.ItemReviewSingleBinding
import com.education.interviewtask.ui.utils.AppUtils

class AdapterReviews(private var reviewsList: List<Reviews>) : RecyclerView.Adapter<AdapterReviews.MyViewHolder>() {
    private lateinit var binding: ItemReviewSingleBinding
    fun setProductsList(reviewsList: List<Reviews>) {
        this.reviewsList = reviewsList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterReviews.MyViewHolder {
        binding = ItemReviewSingleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AdapterReviews.MyViewHolder, position: Int) {
        val reviews = reviewsList[position]
        binding.reviewerName.text = reviews.reviewerName
        binding.date.text = reviews.date?.let { AppUtils.formatIsoDate(it) }
        binding.reviewerEmail.text = reviews.reviewerEmail
        binding.comment.text = reviews.comment
        binding.rating.rating = reviews.rating!!.toFloat()

    }

    override fun getItemCount(): Int {
        return reviewsList.size
    }

    class MyViewHolder(binding: ItemReviewSingleBinding) : RecyclerView.ViewHolder(binding.root)


}
