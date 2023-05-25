package hu.bme.aut.msl_coincapapp.ui.screen.favorites

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.msl_coincapapp.model.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FavoritesRepository,
) : ViewModel() {
    private val _favoriteCurrencies = mutableStateOf(listOf<Currency>())
    val favoriteCurrencies: State<List<Currency>> = _favoriteCurrencies

    private val _isRefresh = MutableStateFlow(false)
    val isRefresh = _isRefresh.asStateFlow()

    init {
        loadFavoriteCurrencies()
    }

    fun refreshFavoriteCurrencies() {
        _isRefresh.value = true
        loadFavoriteCurrencies()
    }

    private fun loadFavoriteCurrencies() = viewModelScope.launch {
        _favoriteCurrencies.value = repository.getFavoriteCurrencies()
        _isRefresh.value = false
    }
}