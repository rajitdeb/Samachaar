package com.rajit.samachaar.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
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

    /**
     * NOTE:
     * If DI wasn't used in this project, then the Thread-safety and Singleton Instance creation
     * would have been done in this way
     */
//    companion object {
//
//        /*
//         * Volatile annotation is used to let all the threads know immediately about this instance
//         * in a multi-threaded environment
//         */
//        @Volatile
//        private var instance: MemeDatabase? = null
//        private var LOCK = Any()
//
//        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
//            instance ?: createDatabase(context).also {
//                instance = it
//            }
//        }
//
//        private fun createDatabase(context: Context) =
//            Room.databaseBuilder(
//                context.applicationContext,
//                MemeDatabase::class.java,
//                DATABASE_NAME
//            ).build()
//
//    }

}