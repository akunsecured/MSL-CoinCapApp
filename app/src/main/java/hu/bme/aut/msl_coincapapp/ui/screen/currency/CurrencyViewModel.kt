package hu.bme.aut.msl_coincapapp.ui.screen.currency

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.model.FavoriteStatus
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrencyViewModel @Inject constructor(
    private val repository: CurrencyRepository
) : ViewModel() {
    private val _currency = mutableStateOf(Currency())
    val currency: State<Currency> get() = _currency

    private val _favoriteStatus = mutableStateOf(FavoriteStatus())
    val favoriteStatus: State<FavoriteStatus> get() = _favoriteStatus

    fun initCurrencyStateById(id: String) = viewModelScope.launch {
        _currency.value = repository.getCurrencyFromDatabase(id)

        var favoriteStatus: FavoriteStatus?
        favoriteStatus = repository.getFavoriteStatusFromDatabase(id)
        if (favoriteStatus == null) {
            favoriteStatus = FavoriteStatus(id)
            repository.insertFavoriteStatusToDatabase(favoriteStatus)
        }
        _favoriteStatus.value = favoriteStatus
    }

    fun updateFavoriteStatus() = viewModelScope.launch {
        _favoriteStatus.value = _favoriteStatus.value.copy(
            isFavorite = !(_favoriteStatus.value.isFavorite)
        )

        repository.updateFavoriteStatus(_favoriteStatus.value)
    }
}