package com.rajit.samachaar.data.local.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.util.Constants
import kotlinx.parcelize.Parcelize

@Entity(tableName = Constants.FAVOURITES_TABLE_NAME)
data class FavouriteArticlesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val article: Article
)