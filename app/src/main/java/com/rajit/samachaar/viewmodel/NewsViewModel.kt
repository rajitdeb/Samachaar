package com.rajit.samachaar.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rajit.samachaar.data.local.entity.Country
import com.rajit.samachaar.data.local.entity.LanguageAndSource
import com.rajit.samachaar.data.repository.DataStoreRepository
import com.rajit.samachaar.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    suspend fun getCountryAndCode(): LiveData<Country> {
        val job = viewModelScope.async(Dispatchers.IO) {
            dataStoreRepository.readCountryAndCode().asLiveData()
        }

        return job.await()
    }

    fun saveCountryAndCode(
        country: String,
        countryCode: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveCountryAndCode(country, countryCode)
    }

    suspend fun getLanguageAndSource(): LiveData<LanguageAndSource> {
        val job = viewModelScope.async(Dispatchers.IO) {
            dataStoreRepository.readLanguageAndSource().asLiveData()
        }

        return job.await()
    }

    fun saveLanguageAndSource(
        language: String, languageId: Int,
        source: String,
        sourceId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveLanguageAndSource(language, languageId, source, sourceId)
    }

    suspend fun getSearchQuery(): LiveData<String> {
        val job = viewModelScope.async(Dispatchers.IO) {
            dataStoreRepository.readSearchQuery().asLiveData()
        }

        return job.await()
    }

    fun setSearchQuery(query: String) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveSearchQuery(query)
    }

    fun resetSearchQuery() = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveSearchQuery("")
    }

}