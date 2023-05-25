package hu.bme.aut.msl_coincapapp.ui.screen.favorites

import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val currencyDao: CurrencyDao,
) {
    suspend fun getFavoriteCurrencies(): List<Currency> = withContext(Dispatchers.IO) {
        currencyDao.getFavoriteCurrencies()
    }
}