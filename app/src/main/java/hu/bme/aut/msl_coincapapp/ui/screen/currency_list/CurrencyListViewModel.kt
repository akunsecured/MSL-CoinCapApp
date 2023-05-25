package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.utils.DefaultPaginator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

enum class SearchWidgetState {
    OPENED,
    CLOSED
}

data class CurrencyListViewState(
    val isLoading: Boolean = false,
    val isFromCache: Boolean = false,
    val currencies: List<Currency> = emptyList(),
    val error: String = "",
    val endReached: Boolean = false,
    val page: Int = 0,
)

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val repository: CurrencyListRepository
) : ViewModel() {
    var viewState by mutableStateOf(CurrencyListViewState())

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh = _isRefresh.asStateFlow()

    private val _searchWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> get() = _searchWidgetState

    private fun getErrorMessage(error: Throwable?) = if (error != null) {
        if (error is HttpException) {
            error.localizedMessage ?: "An unexpected error occurred"
        } else {
            "Couldn't reach server. Check your internet connection"
        }
    } else {
        ""
    }

    private val paginator = DefaultPaginator(
        initialKey = 0,
        onLoadUpdated = {
            viewState = viewState.copy(
                isLoading = it
            )
        },
        onRequest = { nextPage ->
            repository.loadNextCurrencies(nextPage, 20)
        },
        getNextKey = {
            viewState.page + 20
        },
        onError = { error ->
            viewState = viewState.copy(
                error = getErrorMessage(error)
            )
        },
        onSuccess = { currencies, newKey, error, clearPrev ->
            viewState = viewState.copy(
                currencies = if (clearPrev) {
                    currencies
                } else {
                    if (viewState.isFromCache) {
                        val newCurrencies = currencies.toMutableList().filter { currency ->
                            !viewState.currencies.map { c -> c.id }.toList().contains(currency.id)
                        }
                        viewState.currencies + newCurrencies
                    } else {
                        viewState.currencies + currencies
                    }
                },
                page = newKey,
                endReached = currencies.isEmpty(),
                error = getErrorMessage(error)
            )

            if (_isRefresh.value) {
                _isRefresh.value = false
            }
        }
    )

    fun updateSearchWidgetState(newState: SearchWidgetState) {
        _searchWidgetState.value = newState
    }

    private val _searchTextState: MutableState<String> =
        mutableStateOf("")
    val searchTextState: State<String> get() = _searchTextState

    fun updateSearchTextState(newState: String) {
        _searchTextState.value = newState
    }

    init {
        loadNextCurrencies()
    }

    fun loadNextCurrencies(clearPrev: Boolean = false) {
        viewModelScope.launch {
            if (clearPrev) {
                viewState = CurrencyListViewState()
                paginator.reset()
            }
            paginator.loadNextItems(clearPrev)
        }
    }

    fun refreshCurrencyList() {
        _isRefresh.value = true
        loadNextCurrencies(true)
    }

    fun searchCurrencies(text: String) {
        viewModelScope.launch {
            val result = repository.searchCurrencies(text)

            viewState = CurrencyListViewState(
                currencies = result.items ?: emptyList(),
                isFromCache = result.isFromCache,
                error = getErrorMessage(result.error)
            )
        }
    }
}