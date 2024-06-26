package com.ashish.mathongo.utils

sealed class NetworkResult<T>(
    val data: T? = null,
    val message: String? = null,
    val errorCode : Int? = null
) {
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(message: String?, data: T? = null, errorCode : Int? = null) : NetworkResult<T>(data, message, errorCode)
    class Loading<T> : NetworkResult<T>()
}