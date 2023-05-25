package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.network.CoinCapService
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDao
import hu.bme.aut.msl_coincapapp.utils.AppResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrencyListRepository @Inject constructor(
    private val coinCapService: CoinCapService,
    private val currencyDao: CurrencyDao,
) {
    suspend fun loadNextCurrencies(offset: Int, limit: Int): AppResult<Currency> =
        withContext(Dispatchers.IO) {
            delay(250)
            try {
                val results = coinCapService.getAssets(offset = offset, limit = limit)

                val currencyList = results.data
                currencyList.forEach { currency ->
                    currency.timestamp = results.timestamp
                }
                currencyDao.insertCurrencyList(currencyList)

                return@withContext AppResult.Success(items = currencyList)
            } catch (e: Exception) {
                val currenciesFromRoom = currencyDao.getNextCurrencies(
                    offset = offset,
                    limit = limit
                )
                return@withContext if (currenciesFromRoom.isEmpty()) {
                    AppResult.Failure(error = e)
                } else {
                    AppResult.Success(
                        items = currenciesFromRoom,
                        isFromCache = true,
                        error = e
                    )
                }
            }
        }

    suspend fun searchCurrencies(text: String): AppResult<Currency> = withContext(Dispatchers.IO) {
        delay(250)
        try {
            val results = coinCapService.getAssets(search = text)

            val currencyList = results.data
            currencyList.forEach { currency ->
                currency.timestamp = results.timestamp
            }
            currencyDao.insertCurrencyList(currencyList)

            return@withContext AppResult.Success(currencyList)
        } catch (e: Exception) {
            val currenciesFromRoom = currencyDao.getSearchedCurrencies(text)
            return@withContext if (currenciesFromRoom.isEmpty()) {
                AppResult.Failure(error = e)
            } else {
                AppResult.Success(
                    items = currenciesFromRoom,
                    isFromCache = true,
                    error = e
                )
            }
        }
    }
}