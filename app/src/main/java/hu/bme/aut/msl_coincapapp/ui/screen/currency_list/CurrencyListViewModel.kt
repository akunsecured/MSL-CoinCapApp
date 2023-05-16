package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    repository: CurrencyListRepository
) : ViewModel() {
    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> get() = _isLoading

    val currencyList =
        repository.loadCurrencies(
            onStart = {
                _isLoading.value = true
            },
            onCompletion = {
                _isLoading.value = false
            },
            onError = {
                Log.e("CurrencyList", it)
            }
        )
}