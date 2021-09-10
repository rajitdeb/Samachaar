package com.rajit.samachaar.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rajit.samachaar.data.local.entity.Country
import com.rajit.samachaar.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.countryPreferences by preferencesDataStore(Constants.PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    // keys of preferences for easy access
    private object PreferenceKeys {
        val selectedCountry = stringPreferencesKey(Constants.COUNTRY_PREFERENCE_KEY)
        val selectedCountryCode = stringPreferencesKey(Constants.COUNTRY_CODE_PREFERENCE_KEY)
    }

    private val dataStore: DataStore<Preferences> = context.countryPreferences

    // save country data
    suspend fun saveCountryAndCode(
        country: String,
        countryCode: String
    ){
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedCountry] = country
            preferences[PreferenceKeys.selectedCountryCode] = countryCode
        }
    }

    // read country data
    fun readCountryAndCode(): Flow<Country> = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }
        .map { preferences ->
            val selectedCountry = preferences[PreferenceKeys.selectedCountry] ?: Constants.DEFAULT_COUNTRY_PREFERENCES
            val selectedCountryCode = preferences[PreferenceKeys.selectedCountryCode] ?: Constants.DEFAULT_COUNTRY_CODE_PREFERENCES
            Country(
                selectedCountry,
                selectedCountryCode
            )
        }
}