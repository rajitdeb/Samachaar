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
    private val queries: Map<String, String>
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {

        val position = params.key ?: NEWS_STARTING_PAGE_INDEX
        Log.d("Paging Source", "Paging Source: Page: $position")

        return try {

            val response = if(queries[Constants.QUERY_KEY_CATEGORY] != null){
                Log.d("Category", "Category: ${queries[Constants.QUERY_KEY_CATEGORY]}")
                newsApi.getTopCategoryHeadlines(
                    queries[Constants.QUERY_KEY_COUNTRY]!!,
                    queries[Constants.QUERY_KEY_CATEGORY]!!,
                    position,
                    queries[Constants.QUERY_KEY_API_KEY]!!
                )
            }else{
                newsApi.getTopHeadlines(
                    queries[Constants.QUERY_KEY_COUNTRY]!!,
                    position,
                    queries[Constants.QUERY_KEY_API_KEY]!!
                )
            }
            Log.d("News Response", "News Response Response Dissection: ${queries[Constants.QUERY_KEY_CATEGORY]}")
            val article = response.body()!!.articles

            LoadResult.Page(
                data = article,
                prevKey = if (position == NEWS_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (article.isEmpty()) null else position + 1
            )
        } catch (e: Exception) {
            Log.d("News Response", "News Response Error: ${e.message}")
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