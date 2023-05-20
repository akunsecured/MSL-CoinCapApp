package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class SearchWidgetState {
    OPENED,
    CLOSED
}

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    repository: CurrencyListRepository
) : ViewModel() {
    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    private val _isError: MutableState<String?> = mutableStateOf(null)
    val isError: State<String?> get() = _isError

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

    val currencyList =
        repository.loadCurrencies(
            onStart = {
                _isLoading.value = true
            },
            onCompletion = {
                _isLoading.value = false
                _isError.value = null
            },
            onError = {
                _isError.value = it
                Log.e("CurrencyList", it)
            }
        )
}