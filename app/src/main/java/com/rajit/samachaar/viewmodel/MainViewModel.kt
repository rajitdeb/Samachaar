package com.rajit.samachaar.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rajit.samachaar.data.local.entity.FavouriteArticlesEntity
import com.rajit.samachaar.data.network.model.Article
import com.rajit.samachaar.data.repository.Repository
import com.rajit.samachaar.ui.recylcerviewitem.TopHeadlinesRecyclerViewItem
import com.rajit.samachaar.util.Constants
import com.rajit.samachaar.util.Constants.Companion.QUERY_KEY_API_KEY
import com.rajit.samachaar.util.Constants.Companion.QUERY_KEY_CATEGORY
import com.rajit.samachaar.util.Constants.Companion.QUERY_KEY_COUNTRY
import com.rajit.samachaar.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM **/

    var favouriteArticles: LiveData<List<FavouriteArticlesEntity>> =
        repository.local.getAllFavourites().asLiveData()

    fun insertFavourites(favouriteArticlesEntity: FavouriteArticlesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertArticle(favouriteArticlesEntity)
        }

    fun deleteFavouriteArticle(favouriteArticlesEntity: FavouriteArticlesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteArticle(favouriteArticlesEntity)
        }

    /** RETROFIT **/

    private var queries: HashMap<String, String> = HashMap()

    var topHeadlines = MutableLiveData<PagingData<Article>>()

    var topCategoryHeadlines = MutableLiveData<PagingData<Article>>()

    fun getTopHeadlines(){
        topHeadlines = repository.remote.getTopHeadlines(queries).cachedIn(viewModelScope).asLiveData()
                as MutableLiveData<PagingData<Article>>
    }

    fun getTopCategoryHeadlines(){
        topCategoryHeadlines = repository.remote.getTopCategoryHeadlines(queries).cachedIn(viewModelScope).asLiveData()
                as MutableLiveData<PagingData<Article>>
    }

    // this applies the queries to the api
    fun applyQueries(country: String, category: String? = null){
        queries[QUERY_KEY_COUNTRY] = country
        if(category != null){
            queries[QUERY_KEY_CATEGORY] = category
        }
        queries[QUERY_KEY_API_KEY] = Constants.QUERY_VALUE_API_KEY
    }

    // code for checking internet connectivity
    @RequiresApi(Build.VERSION_CODES.M)
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}