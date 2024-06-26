package com.ashish.mathongo.data.repo

import com.ashish.mathongo.data.api.RecipesAPI
import com.ashish.mathongo.data.models.Recipe
import com.ashish.mathongo.data.models.RecipeApiResp
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


}
