package com.ashish.mathongo.data.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourite_recipes")
data class RecipeEntity (
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val readyInMinutes : Int,
    val image : String
)