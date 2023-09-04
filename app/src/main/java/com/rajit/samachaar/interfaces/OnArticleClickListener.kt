package com.rajit.samachaar.interfaces

import com.rajit.samachaar.data.network.model.Article

interface OnArticleClickListener {
    fun onArticleClick(article: Article?, category: String)
}