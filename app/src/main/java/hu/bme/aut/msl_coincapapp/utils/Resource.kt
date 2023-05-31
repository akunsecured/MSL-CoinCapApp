package hu.bme.aut.msl_coincapapp.utils

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null,
    val isFromCache: Boolean = false
) {
    class Success<T>(data: T, isFromCache: Boolean = false, message: String? = null) :
        Resource<T>(data, isFromCache = isFromCache, message = message)

    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}