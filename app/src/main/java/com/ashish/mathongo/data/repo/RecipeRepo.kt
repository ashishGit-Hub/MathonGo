package com.ashish.mathongo.data.repo

import com.ashish.mathongo.data.api.RecipesAPI
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.data.models.RecipeApiResp
import com.ashish.mathongo.data.models.SearchResp
import com.ashish.mathongo.data.models.SimilarRecipe
import retrofit2.Response
import javax.inject.Inject

class RecipeRepo @Inject constructor(
    private val recipesAPI: RecipesAPI
) {

    suspend fun getRandomRecipes(query: Map<String,String>): Response<RecipeApiResp> {
        return recipesAPI.getRandomRecipes(query)
    }


    suspend fun getRecipeInfo(recipeId : Int,query: Map<String,String>): Response<Recipe> {
        return recipesAPI.getRecipeInfo(recipeId,query)
    }
    suspend fun searchRecipes(query: Map<String,String>): Response<SearchResp> {
        return recipesAPI.searchRecipes(query)
    }

    suspend fun getSimilarRecipe(recipeId: Int,query: Map<String,String>): Response<List<SimilarRecipe>> {
        return recipesAPI.getSimilarRecipe(recipeId,query)
    }

}
