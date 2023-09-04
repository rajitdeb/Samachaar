package com.rajit.samachaar.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rajit.samachaar.R
import com.rajit.samachaar.data.local.entity.FavouriteArticlesEntity
import com.rajit.samachaar.databinding.SmallArticleItemRowBinding
import com.rajit.samachaar.interfaces.OnArticleClickListener
import com.rajit.samachaar.util.NewsDiffUtil

class FavouritesAdapter(
    private val onArticleClickListener: OnArticleClickListener
) : RecyclerView.Adapter<FavouritesAdapter.MyViewHolder>() {

    private var favouritesList: List<FavouriteArticlesEntity> = emptyList()

    class MyViewHolder(val binding: SmallArticleItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = SmallArticleItemRowBinding.inflate(inflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentFavourite = favouritesList[position]
        if (currentFavourite.article.urlToImage != null) {
            holder.binding.articleImage.load(currentFavourite.article.urlToImage) {
                crossfade(true)
                crossfade(1000)
                placeholder(R.drawable.article_thumbnail_placeholder)
                error(R.drawable.error_placeholder)
            }
        } else {
            holder.binding.articleImage.setImageResource(R.drawable.error_placeholder)
        }

        holder.binding.articleTitle.text = currentFavourite.article.title
        holder.binding.articleDescription.text = currentFavourite.article.description

        holder.itemView.setOnClickListener {
            onArticleClickListener.onArticleClick(
                currentFavourite.article,
                currentFavourite.category
            )
        }
    }

    override fun getItemCount(): Int {
        return favouritesList.size
    }

    fun setData(newList: List<FavouriteArticlesEntity>) {
        val articlesDiffUtil = NewsDiffUtil(favouritesList, newList)
        val diffUtilResult = DiffUtil.calculateDiff(articlesDiffUtil)
        favouritesList = newList
        diffUtilResult.dispatchUpdatesTo(this)
    }

}