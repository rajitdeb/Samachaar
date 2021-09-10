package com.rajit.samachaar.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.rajit.samachaar.util.Constants

@Entity(tableName = Constants.COUNTRY_TABLE_NAME)
data class Country(
    @PrimaryKey val countryName: String,
    val countryCode: String
)
