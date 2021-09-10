package com.rajit.samachaar.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.rajit.samachaar.data.local.dao.ArticlesDao
import com.rajit.samachaar.data.local.database.typeconverters.ArticlesTypeConverter
import com.rajit.samachaar.data.local.entity.Country
import com.rajit.samachaar.data.local.entity.FavouriteArticlesEntity

@Database(
    entities = [FavouriteArticlesEntity::class, Country::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(ArticlesTypeConverter::class)
abstract class FavouriteArticlesDatabase: RoomDatabase() {

    abstract fun favouriteArticleDao(): ArticlesDao

}