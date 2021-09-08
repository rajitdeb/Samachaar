package com.rajit.samachaar.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.rajit.samachaar.data.network.api.ArticlesApi
import com.rajit.samachaar.data.network.pagingsource.NewsPagingSource
import com.rajit.samachaar.util.Constants
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val articlesApi: ArticlesApi
) {

    fun getTopHeadlines(queries: Map<String, String>) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(articlesApi, queries) }
        ).flow

    fun getTopCategoryHeadlines(queries: Map<String, String>) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(articlesApi, queries) }
        ).flow
}