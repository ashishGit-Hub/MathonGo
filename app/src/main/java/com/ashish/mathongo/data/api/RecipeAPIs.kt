package com.ashish.mathongo.data.api

import com.ashish.mathongo.Constants.RANDOM_RECIPE
import com.ashish.mathongo.data.models.RecipeApiResp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RecipeAPIs {

    @GET(RANDOM_RECIPE)
    suspend fun getRandomRecipes(@QueryMap query : Map<String,Any>): Response<RecipeApiResp>

}