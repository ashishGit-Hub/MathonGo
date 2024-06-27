package com.ashish.mathongo.data.api

import com.ashish.mathongo.Constants.RANDOM_RECIPE
import com.ashish.mathongo.Constants.SEARCH_RECIPE
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.data.models.RecipeApiResp
import com.ashish.mathongo.data.models.SearchResp
import com.ashish.mathongo.data.models.SimilarRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface RecipesAPI {

    @GET(RANDOM_RECIPE)
    suspend fun getRandomRecipes(@QueryMap query : Map<String,String>): Response<RecipeApiResp>

    @GET(RANDOM_RECIPE)
    suspend fun getSimilarRecipes(@QueryMap query : Map<String,String>): Response<RecipeApiResp>

    @GET("{id}/information")
    suspend fun getRecipeInfo(@Path("id") recipeId : Int, @QueryMap query : Map<String,String>): Response<Recipe>

    @GET(SEARCH_RECIPE)
    suspend fun searchRecipes(@QueryMap query : Map<String,String>) : Response<SearchResp>

    @GET("{id}/similar")
    suspend fun getSimilarRecipe(@Path("id") recipeId : Int, @QueryMap query : Map<String,String>): Response<List<SimilarRecipe>>

}