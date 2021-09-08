package com.rajit.samachaar.data.local.dao

import androidx.room.*
import com.rajit.samachaar.data.local.entity.FavouriteArticlesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticlesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertArticles(favouriteArticlesEntity: FavouriteArticlesEntity)

    @Delete
    suspend fun deleteArticle(favouriteArticlesEntity: FavouriteArticlesEntity)

    @Query("SELECT * FROM FAVOURITES_TABLE")
    fun getAllArticles(): Flow<List<FavouriteArticlesEntity>>

}