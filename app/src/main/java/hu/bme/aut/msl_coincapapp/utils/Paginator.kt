package hu.bme.aut.msl_coincapapp.utils

interface Paginator<Key, Item> {
    suspend fun loadNextItems(clearPrev: Boolean)
    fun reset()
}