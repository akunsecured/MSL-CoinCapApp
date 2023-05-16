package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import androidx.annotation.WorkerThread
import hu.bme.aut.msl_coincapapp.network.CoinCapService
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CurrencyListRepository @Inject constructor(
    private val coinCapService: CoinCapService,
    private val currencyDao: CurrencyDao,
) {
    @WorkerThread
    fun loadCurrencies(
        onStart: () -> Unit,
        onCompletion: () -> Unit,
        onError: (String) -> Unit
    ) = flow {
        val currencies = currencyDao.getAllCurrencies()
        if (currencies.isEmpty()) {
            coinCapService.getAssets().apply {
                currencyDao.insertCurrencyList(data)
                emit(data)
            }
        } else {
            emit(currencies)
        }
    }.onStart { onStart() }
        .onCompletion { onCompletion() }
        .catch { e ->
            onError(e.message ?: "Something went wrong")
        }
        .flowOn(Dispatchers.IO)
}