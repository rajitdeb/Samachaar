package com.rajit.samachaar.util

import com.rajit.samachaar.R
import com.rajit.samachaar.data.network.model.Category

class Constants {

    companion object {

        const val BASE_URL = "https://newsapi.org/"

        const val NEWS_STARTING_PAGE_INDEX = 1
        const val QUERY_KEY_COUNTRY = "country"
        const val QUERY_KEY_CATEGORY = "category"
        const val QUERY_KEY_API_KEY = "apiKey"

        // Default Queries
        const val QUERY_VALUE_COUNTRY = "in"
        const val QUERY_VALUE_CATEGORY = "business"
        const val QUERY_VALUE_API_KEY = "YOUR_API_KEY"

        // Exceptions Log Key
        const val PAGING_SOURCE_PAGE_KEY = "Paging Source"
        const val NEWS_RESPONSE_KEY = "News Response"


        // ROOM
        const val FAVOURITES_TABLE_NAME = "favourites_table"
        const val COUNTRY_TABLE_NAME = "country_table"
        const val FAVOURITES_DB_NAME = "news_db"

        // PREFERENCES
        const val PREFERENCES_NAME = "countryPreferences"
        const val COUNTRY_PREFERENCE_KEY = "country"
        const val COUNTRY_CODE_PREFERENCE_KEY = "countryCode"

        const val DEFAULT_COUNTRY_PREFERENCES = "India"
        const val DEFAULT_COUNTRY_CODE_PREFERENCES = "in"

        const val LANGUAGE_PREFERENCE_KEY = "language"
        const val LANGUAGE_ID_PREFERENCE_KEY = "languageId"
        const val SOURCE_PREFERENCE_KEY = "sources"
        const val SOURCE_ID_PREFERENCE_KEY = "sourcesId"
        const val DEFAULT_LANGUAGE_PREFERENCE_VALUE = "en"
        const val DEFAULT_SOURCE_PREFERENCE_VALUE = "all"
        const val DEFAULT_SEARCH_QUERY_PREFERENCE_VALUE = ""

        val listOfCategory = listOf(
            Category("Business", R.drawable.business),
            Category("Entertainment", R.drawable.entertainment),
            Category("Health", R.drawable.health),
            Category("Sports", R.drawable.sports),
            Category("Science", R.drawable.science),
            Category("Technology", R.drawable.technology)
        )

        const val SEARCH_QUERY_PREFERENCE_KEY = "searchQuery"

    }

}