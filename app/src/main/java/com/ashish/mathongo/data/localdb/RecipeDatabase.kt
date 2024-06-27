package com.ashish.mathongo.data.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RecipeEntity::class], version = 1,)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao() : RecipeDao

    companion object{
        @Volatile private var instance: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "favourite_recipes"
                ).build().also { instance = it }
            }
    }

}