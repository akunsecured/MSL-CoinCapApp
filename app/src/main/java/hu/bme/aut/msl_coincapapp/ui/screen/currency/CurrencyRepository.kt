package hu.bme.aut.msl_coincapapp.ui.screen.currency

import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyDao: CurrencyDao,
) {
    suspend fun getCurrencyFromDatabase(id: String): Currency = withContext(Dispatchers.IO) {
        return@withContext currencyDao.getCurrencyById(id) ?: Currency()
    }

    suspend fun updateCurrency(currency: Currency) = withContext(Dispatchers.IO) {
        currencyDao.update(currency)
    }
}