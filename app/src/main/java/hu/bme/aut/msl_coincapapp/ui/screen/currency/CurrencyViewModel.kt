package hu.bme.aut.msl_coincapapp.ui.screen.currency

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.msl_coincapapp.model.Currency
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {
    private val _currency = mutableStateOf(Currency())
    val currency: State<Currency> get() = _currency

    fun initCurrencyStateById(id: String) = viewModelScope.launch {
        _currency.value = repository.getCurrencyFromDatabase(id)
    }

    fun updateFavoriteStatus() = viewModelScope.launch {
        _currency.value = _currency.value.copy(
            isFavorite = !(_currency.value.isFavorite)
        )

        repository.updateCurrency(_currency.value)
    }
}