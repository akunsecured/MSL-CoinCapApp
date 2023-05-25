package hu.bme.aut.msl_coincapapp.ui.screen.currency

import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.model.FavoriteStatus
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDao
import hu.bme.aut.msl_coincapapp.persistence.FavoriteStatusDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyRepository @Inject constructor(
    private val currencyDao: CurrencyDao,
    private val favoriteStatusDao: FavoriteStatusDao,
) {
    suspend fun getCurrencyFromDatabase(id: String): Currency = withContext(Dispatchers.IO) {
        return@withContext currencyDao.getCurrencyById(id) ?: Currency()
    }

    suspend fun getFavoriteStatusFromDatabase(id: String): FavoriteStatus? =
        withContext(Dispatchers.IO) {
            return@withContext favoriteStatusDao.getFavoriteStatusById(id)
        }

    suspend fun insertFavoriteStatusToDatabase(favoriteStatus: FavoriteStatus) =
        withContext(Dispatchers.IO) {
            favoriteStatusDao.insertFavoriteStatus(favoriteStatus)
        }

    suspend fun updateFavoriteStatus(favoriteStatus: FavoriteStatus) = withContext(Dispatchers.IO) {
        favoriteStatusDao.updateFavoriteStatus(favoriteStatus)
    }
}