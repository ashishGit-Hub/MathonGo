package com.ashish.mathongo.data.api

import com.ashish.mathongo.Constants.RANDOM_RECIPE
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.data.models.RecipeApiResp
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RecipesAPI {

    @GET(RANDOM_RECIPE)
    suspend fun getRandomRecipes(@QueryMap query : Map<String,String>): Response<RecipeApiResp>

    @GET("{id}/information")
    suspend fun getRecipeInfo(@Path("id") recipeId : Int, @QueryMap query : Map<String,String>): Response<Recipe>

}