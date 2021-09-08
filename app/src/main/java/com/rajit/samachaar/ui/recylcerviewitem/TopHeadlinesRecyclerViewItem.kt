package com.rajit.samachaar.ui.recylcerviewitem

import com.rajit.samachaar.data.network.model.Article

sealed class TopHeadlinesRecyclerViewItem {

    class Title(
        val id: Int,
        val title: String
    ): TopHeadlinesRecyclerViewItem()

    class BigArticle(
        val article: Article
    ): TopHeadlinesRecyclerViewItem()

    class SmallArticle(
        val article: Article
    ): TopHeadlinesRecyclerViewItem()

}