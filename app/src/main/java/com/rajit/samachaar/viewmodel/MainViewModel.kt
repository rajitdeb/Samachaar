package com.rajit.samachaar.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rajit.samachaar.data.local.entity.Country
import com.rajit.samachaar.data.local.entity.FavouriteArticlesEntity
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.data.repository.Repository
import com.rajit.samachaar.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    /** ROOM **/

    suspend fun getAllCountriesList(): LiveData<List<Country>> {
        val job = viewModelScope.async(Dispatchers.IO) {
            repository.local.getAllCountries().asLiveData()
        }

        return job.await()
    }

    suspend fun getAllFavourites(): LiveData<List<FavouriteArticlesEntity>> {
        val job = viewModelScope.async(Dispatchers.IO) {
            repository.local.getAllFavourites().asLiveData()
        }

        return job.await()
    }

    fun insertFavourites(
        favouriteArticlesEntity: FavouriteArticlesEntity
    ) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertArticle(favouriteArticlesEntity)
    }

    fun deleteFavouriteArticle(
        favouriteArticlesEntity: FavouriteArticlesEntity
    ) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteArticle(favouriteArticlesEntity)
    }

    fun deleteAllFavourites() = viewModelScope.launch(Dispatchers.IO) {
        repository.local.deleteAllFavourites()
    }

    /** RETROFIT **/

    fun getTopHeadlines(
        query_country: String
    ): LiveData<PagingData<Article>> {
        Log.d("News Main ViewModel", "News Main ViewModel: $query_country")
        return repository
            .remote
            .getTopHeadlines(query_country, Constants.QUERY_VALUE_API_KEY)
            .cachedIn(viewModelScope).asLiveData()
    }

    fun getTopCategoryHeadlines(
        query_country: String,
        query_category: String
    ): LiveData<PagingData<Article>> {
        return repository
            .remote
            .getTopCategoryHeadlines(query_country, query_category, Constants.QUERY_VALUE_API_KEY)
            .cachedIn(viewModelScope).asLiveData()
    }

    fun searchArticle(
        searchQuery: String,
        language: String,
        source: String? = null
    ): LiveData<PagingData<Article>> {
        return if (source != null) {
            repository
                .remote
                .searchArticle(searchQuery, language, source, Constants.QUERY_VALUE_API_KEY)
                .cachedIn(viewModelScope).asLiveData()
        } else {
            repository
                .remote
                .searchArticle(searchQuery, language, query_apiKey = Constants.QUERY_VALUE_API_KEY)
                .cachedIn(viewModelScope).asLiveData()
        }

    }
}