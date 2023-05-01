package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.msl_coincapapp.network.CoinCapResult
import javax.inject.Inject

sealed interface CurrencyListState {
    data class Success(val coinCapResult: CoinCapResult) : CurrencyListState
    object Error : CurrencyListState
    object Loading : CurrencyListState
}

@HiltViewModel
class CurrencyListViewModel @Inject constructor(): ViewModel()