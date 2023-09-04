package com.rajit.samachaar.data.local.entity

data class LanguageAndSource(
    val lang: String,
    val languageId: Int,
    val source: String = "all",
    val sourceId: Int
)
