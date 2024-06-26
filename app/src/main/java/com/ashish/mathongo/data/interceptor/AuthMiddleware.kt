package com.ashish.mathongo.data.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthMiddleware @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val urlWithApiKey =
            request.url.newBuilder()
                .addQueryParameter("apiKey", "3f08e781bfd74b0b8b09b3dc8cfb440f")
                .build()
        val newRequest = request.newBuilder()
            .url(urlWithApiKey)
            .build()
        return chain.proceed(newRequest)
    }


}