package com.ashish.mathongo.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.ashish.mathongo.data.localdb.RecipeDao
import com.ashish.mathongo.data.localdb.RecipeDatabase
import com.ashish.mathongo.utils.NetworkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideNetConnection(application: Application): NetworkManager =
        NetworkManager(application)


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): RecipeDatabase {
        return Room.databaseBuilder(
            appContext,
            RecipeDatabase::class.java,
            "contact_database"
        ).build()
    }

    @Provides
    fun provideContactDao(database: RecipeDatabase): RecipeDao {
        return database.recipeDao()
    }
}