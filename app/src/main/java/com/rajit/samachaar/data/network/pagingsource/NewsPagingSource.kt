package com.rajit.samachaar.data.network.pagingsource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rajit.samachaar.data.network.api.ArticlesApi
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.util.Constants
import com.rajit.samachaar.util.Constants.Companion.NEWS_STARTING_PAGE_INDEX

class NewsPagingSource(
    private val newsApi: ArticlesApi,
    private val query_country: String? = null,
    private val query_category: String? = null,
    private val query_apiKey: String,
    private val searchQuery: String? = null
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        val position = params.key ?: NEWS_STARTING_PAGE_INDEX
        Log.d("Paging Source", "Paging Source: Page: $position")

        return try {

            val response = if(query_category != null){
//                Log.d("Category", "News ViewModel: category: $query_category")
                newsApi.getTopCategoryHeadlines(
                    query_country!!,
                    query_category,
                    position,
                    query_apiKey
                )
            }else if(query_country != null && searchQuery == null){
//                    Log.d("Category", "News ViewModel: called getTopHeadline()")
                    newsApi.getTopHeadlines(
                        query_country,
                        position,
                        query_apiKey
                    )
            }else{
                newsApi.searchArticle(searchQuery!!, position, query_apiKey)
            }
            val article = response.body()!!.articles
//            Log.d("News Response", "News Response Error: $article")
//            Log.d("News Response", "News Response Error: ${response.body()}")

            LoadResult.Page(
                data = article,
                prevKey = if (position == NEWS_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (article.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.d("News Response", "News Response Error: $e")
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}