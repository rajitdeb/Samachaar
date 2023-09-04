package com.rajit.samachaar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rajit.samachaar.data.network.model.Category
import com.rajit.samachaar.databinding.CategoryItemBinding
import com.rajit.samachaar.interfaces.OnCategoryClickListener

class CategoryAdapter(
    private val onCategoryClickListener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    private var categoryList: List<Category> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCategory = categoryList[position]
        holder.bind(currentCategory)

    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    inner class MyViewHolder(
        val binding: CategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(currentCategory: Category) {
            binding.apply {
                categoryName.text = currentCategory.name
                categoryImage.setImageResource(currentCategory.resourceId)
            }

            binding.view.setOnClickListener {
                onCategoryClickListener.onClick(currentCategory.name)
            }

        }

    }

    fun setData(list: List<Category>) {
        categoryList = list
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

}