package com.ashish.mathongo.data.localdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecipeDao {

    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity)
    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)


    @Query("SELECT * FROM favourite_recipes ORDER BY id ASC")
    fun getRecipe() : LiveData<List<RecipeEntity>>

    @Query("SELECT * FROM favourite_recipes WHERE id = :recipeId LIMIT 1")
    suspend fun isFavoriteRecipe(recipeId: Int): RecipeEntity?

}