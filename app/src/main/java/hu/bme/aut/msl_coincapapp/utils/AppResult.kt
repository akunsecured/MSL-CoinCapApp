package hu.bme.aut.msl_coincapapp.utils

sealed class AppResult<T>(
    val items: List<T>? = null,
    val error: Throwable? = null,
    val isFromCache: Boolean = false,
) {
    class Success<T>(
        items: List<T>,
        error: Throwable? = null,
        isFromCache: Boolean = false
    ) : AppResult<T>(items, error, isFromCache)

    class Failure<T>(
        items: List<T>? = null,
        error: Throwable?,
    ) : AppResult<T>(items, error)
}