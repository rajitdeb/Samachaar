package com.rajit.samachaar.di

import android.content.Context
import androidx.room.Room
import com.rajit.samachaar.data.local.database.FavouriteArticlesDatabase
import com.rajit.samachaar.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        FavouriteArticlesDatabase::class.java,
        Constants.FAVOURITES_DB_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: FavouriteArticlesDatabase) = database.favouriteArticleDao()

}