package hu.bme.aut.msl_coincapapp.utils

class DefaultPaginator<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> AppResult<Item>,
    private inline val getNextKey: suspend (List<Item>) -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key, error: Throwable?, clearPrev: Boolean) -> Unit
): Paginator<Key, Item> {
    private var currentKey: Key = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems(clearPrev: Boolean) {
        if (isMakingRequest) return

        isMakingRequest = true
        onLoadUpdated(true)

        val result = onRequest(currentKey)

        isMakingRequest = false

        val items = result.items
        if (items == null && result.error != null) {
            onError(result.error)
            onLoadUpdated(false)
            return
        }

        currentKey = getNextKey(items!!)
        onSuccess(items, currentKey, result.error, clearPrev)
        onLoadUpdated(false)
    }

    override fun reset() {
        currentKey = initialKey
    }
}