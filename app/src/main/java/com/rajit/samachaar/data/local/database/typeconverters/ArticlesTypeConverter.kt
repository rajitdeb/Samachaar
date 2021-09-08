package com.rajit.samachaar.data.local.database.typeconverters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rajit.samachaar.data.network.model.Article

class ArticlesTypeConverter {

    private val gson = Gson()

    @TypeConverter
    fun articlesToString(article: Article): String{
        return gson.toJson(article)
    }

    @TypeConverter
    fun stringToArticle(data: String): Article {
        val listType = object : TypeToken<Article>() {}. type
        return gson.fromJson(data, listType)
    }

}