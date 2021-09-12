package com.rajit.samachaar.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rajit.samachaar.R
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.databinding.BigArticleItemRowBinding
import com.rajit.samachaar.interfaces.OnArticleClickListener

class MyNewsAdapter(private val onArticleClickListener: OnArticleClickListener) :
    PagingDataAdapter<Article, MyNewsAdapter.MyTopHeadlinesViewHolder>(ARTICLE_COMPARATOR) {

    class MyTopHeadlinesViewHolder(val binding: BigArticleItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentArticle: Article) {
            binding.apply {
                if (currentArticle.urlToImage != null) {
                    binding.bigArticleThumbnail.load(currentArticle.urlToImage) {
                        crossfade(true)
                        crossfade(500)
                        placeholder(R.drawable.article_thumbnail_placeholder)
                        error(R.drawable.error_placeholder)
                    }
                } else {
                    bigArticleThumbnail.setImageResource(R.drawable.error_placeholder)
                    cardView.strokeColor = ContextCompat.getColor(binding.root.context , R.color.color_accent)
                    cardView.strokeWidth = 5
                }
                binding.bigArticleTitle.text = currentArticle.title
                binding.bigArticleSource.text = currentArticle.source?.name

                val publishedAtFull = currentArticle.publishedAt
                if (publishedAtFull != null) {
                    val publishedSplit = publishedAtFull.split("T")
                    val publishedSplit2 = publishedSplit[1].split("Z")

                    binding.bigArticlePublishedAtTv.text =
                        "${publishedSplit[0]}, ${publishedSplit2[0]} UTC"
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyTopHeadlinesViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = BigArticleItemRowBinding.inflate(inflater, parent, false)
                return MyTopHeadlinesViewHolder(binding)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyTopHeadlinesViewHolder, position: Int) {
        val currentArticle = getItem(position)
        if (currentArticle != null) {

            holder.bind(currentArticle)

            holder.itemView.setOnClickListener {
                onArticleClickListener.onArticleClick(currentArticle, "Top Headlines")
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyTopHeadlinesViewHolder {
        return MyTopHeadlinesViewHolder.from(parent)
    }

    companion object {
        val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {

            override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
                return oldItem.url == newItem.url
            }

        }
    }

}