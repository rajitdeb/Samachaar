package com.rajit.samachaar.data.network.api

import com.rajit.samachaar.data.network.model.NewsArticles
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String,
    ): Response<NewsArticles>

    @GET("v2/top-headlines")
    suspend fun getTopCategoryHeadlines(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String,
    ): Response<NewsArticles>

    @GET("v2/everything")
    suspend fun searchArticle(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ): Response<NewsArticles>

    @GET("v2/everything")
    suspend fun searchArticleWithLanguage(
        @Query("q") query: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ): Response<NewsArticles>

    @GET("v2/everything")
    suspend fun searchArticleWithLanguageAndSource(
        @Query("q") query: String,
        @Query("language") language: String,
        @Query("sources") source: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ): Response<NewsArticles>
}