package com.rajit.samachaar.util

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
        const val QUERY_VALUE_API_KEY = "3ca0f15f8bb241628b311a82029566bc"

        // Exceptions Log Key
        const val PAGING_SOURCE_PAGE_KEY = "Paging Source"
        const val NEWS_RESPONSE_KEY = "News Response"


        // ROOM
        const val FAVOURITES_TABLE_NAME = "favourites_table"
        const val FAVOURITES_DB_NAME = "favourites_db"

    }

}