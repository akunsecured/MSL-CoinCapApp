package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SearchWidgetState {
    OPENED,
    CLOSED
}

data class CurrencyListViewState(
    val isLoading: Boolean = false,
    val isFromCache: Boolean = false,
    val currencies: List<Currency> = emptyList(),
    val error: String = ""
)

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    private val repository: CurrencyListRepository
) : ViewModel() {
    private val _viewState = mutableStateOf(CurrencyListViewState())
    val viewState: State<CurrencyListViewState> get() = _viewState

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh = _isRefresh.asStateFlow()

    private val _searchWidgetState: MutableState<SearchWidgetState> =
        mutableStateOf(SearchWidgetState.CLOSED)
    val searchWidgetState: State<SearchWidgetState> get() = _searchWidgetState

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
        getCurrencyList()
    }

    fun refreshCurrencyList() {
        viewModelScope.launch {
            _isRefresh.value = true
            getCurrencyList()
        }
    }

    private fun getCurrencyList() {
        repository.loadCurrencies().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _viewState.value =
                        CurrencyListViewState(
                            currencies = result.data ?: emptyList(),
                            isFromCache = result.isFromCache,
                            error = result.message ?: ""
                        )
                    _isRefresh.value = false
                }

                is Resource.Error -> {
                    _viewState.value = CurrencyListViewState(
                        error = result.message ?: "An unexpected error occured"
                    )
                    _isRefresh.value = false
                }

                is Resource.Loading -> {
                    _viewState.value = CurrencyListViewState(
                        isLoading = true,
                        currencies = _viewState.value.currencies
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}