package com.example.testdanamon.utils

sealed class Resource<out T> (
        val data: T? = null,
        val message: String? = null,
        val code: Int = 200,
        val requestId: String = "",
    ) {
    class Loading<T> : Resource<T>(code = 0)
    class Success<T>(data: T?, requestId: String = "") : Resource<T>(data, requestId = requestId)
    class Error<T>(
        message: String?,
        data: T? = null,
        code: Int = 500,
        requestIdHeader: String = "",
    ) : Resource<T>(data, message, code, requestIdHeader)
}
