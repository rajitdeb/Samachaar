package com.rajit.samachaar.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.rajit.samachaar.data.network.api.ArticlesApi
import com.rajit.samachaar.data.network.pagingsource.NewsPagingSource
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val articlesApi: ArticlesApi
) {

    fun getTopHeadlines(
        query_country: String,
        query_apiKey: String
    ) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    articlesApi,
                    query_country = query_country,
                    query_apiKey = query_apiKey
                )
            }
        ).flow

    fun getTopCategoryHeadlines(
        query_country: String,
        query_category: String,
        query_apiKey: String
    ) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    articlesApi,
                    query_country,
                    query_category,
                    query_apiKey
                )
            }
        ).flow

    fun searchArticle(
        searchQuery: String,
        language: String,
        source: String? = null,
        query_apiKey: String
    ) =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                NewsPagingSource(
                    articlesApi, searchQuery = searchQuery,
                    query_language = language, query_source = source, query_apiKey = query_apiKey
                )
            }
        ).flow
}