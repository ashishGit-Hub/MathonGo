package com.ashish.mathongo.di

import com.ashish.mathongo.Constants.BASE_URL
import com.ashish.mathongo.data.api.RecipesAPI
import com.ashish.mathongo.data.interceptor.AuthMiddleware
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    private val interceptor =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient =
        OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).addInterceptor(interceptor)
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
    }


    // middleware Provider
    @Singleton
    @Provides
    fun provideOkHttpAuth(auth: AuthMiddleware): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).addInterceptor(auth)
            .build()
    }
    @Singleton
    @Provides
    fun providesRecipesAPI(retrofit: Retrofit.Builder, okHttpClient: OkHttpClient): RecipesAPI {
        return retrofit.client(okHttpClient).build().create(RecipesAPI::class.java)
    }


}