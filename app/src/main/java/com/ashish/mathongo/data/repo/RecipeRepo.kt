package com.ashish.mathongo.data.repo

import com.ashish.mathongo.data.api.RecipeAPIs
import com.ashish.mathongo.data.models.RecipeApiResp
import retrofit2.Response
import javax.inject.Inject

class RecipeRepo @Inject constructor(
    private val recipeAPIs: RecipeAPIs
) {

    suspend fun getRandomRecipes(query: Map<String,Any>): Response<RecipeApiResp> {
        return recipeAPIs.getRandomRecipes(query)
    }

}
