package com.rajit.samachaar.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.rajit.samachaar.data.repository.DataStoreRepository
import com.rajit.samachaar.util.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    val readCountryAndCode = dataStoreRepository.readCountryAndCode()

    fun saveCountryAndCode(
        country: String,
        countryCode: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveCountryAndCode(country, countryCode)
    }

    val readLanguageAndSource = dataStoreRepository.readLanguageAndSource()

    fun saveLanguageAndSource(
        language: String, languageId: Int,
        source: String,
        sourceId: Int
    ) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveLanguageAndSource(language, languageId, source, sourceId)
    }

    var readSearchQuery = dataStoreRepository.readSearchQuery()

    fun setSearchQuery(query: String) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveSearchQuery(query)
    }

    fun resetSearchQuery() = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveSearchQuery("")
    }

}