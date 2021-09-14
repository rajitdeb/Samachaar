package com.rajit.samachaar.data.repository

import com.rajit.samachaar.data.local.dao.ArticlesDao
import com.rajit.samachaar.data.local.entity.Country
import com.rajit.samachaar.data.local.entity.FavouriteArticlesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val articlesDao: ArticlesDao
) {

    fun getAllCountries(): Flow<List<Country>> {
        return articlesDao.getAllCountries()
    }

    suspend fun insertArticle(favouriteArticlesEntity: FavouriteArticlesEntity){
        articlesDao.insertArticles(favouriteArticlesEntity)
    }

    suspend fun deleteArticle(favouriteArticlesEntity: FavouriteArticlesEntity) {
        articlesDao.deleteArticle(favouriteArticlesEntity)
    }

    suspend fun deleteAllFavourites() {
        articlesDao.deleteAllFavourites()
    }

    fun getAllFavourites(): Flow<List<FavouriteArticlesEntity>> {
        return articlesDao.getAllArticles()
    }

}