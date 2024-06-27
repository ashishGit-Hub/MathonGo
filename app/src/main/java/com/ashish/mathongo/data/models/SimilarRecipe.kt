package com.ashish.mathongo.data.models

data class SimilarRecipe (
    val id : Int,
    val title : String,
    val imageType : String,
    val readyInMinutes : String,
    val servings : Int,
    val sourceUrl : String
)
