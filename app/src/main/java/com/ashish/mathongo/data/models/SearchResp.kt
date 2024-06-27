package com.ashish.mathongo.data.models

data class SearchResp(
    val offset : Int,
    val number : Int,
    val results : List<SearchedItem>,
    val totalResults : Int
)
