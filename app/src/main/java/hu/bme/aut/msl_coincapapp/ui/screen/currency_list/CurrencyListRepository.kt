package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import androidx.annotation.WorkerThread
import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.network.CoinCapService
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDao
import hu.bme.aut.msl_coincapapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject

class CurrencyListRepository @Inject constructor(
    private val coinCapService: CoinCapService,
    private val currencyDao: CurrencyDao,
) {
    @WorkerThread
    fun loadCurrencies(): Flow<Resource<List<Currency>>> = flow {
        emit(Resource.Loading())

        try {
            val results = coinCapService.getAssets()

            val currencyList = results.data
            currencyList.forEach { currency ->
                currency.timestamp = results.timestamp
            }
            currencyDao.insertCurrencyList(currencyList)

            emit(Resource.Success(currencyList))
        } catch (e: Exception) {
            val currenciesFromRoom = currencyDao.getAllCurrencies()
            if (currenciesFromRoom.isEmpty()) {
                if (e is HttpException) {
                    emit(Resource.Error(e.localizedMessage ?: "An unexpected error occured"))
                    return@flow
                }

                emit(Resource.Error("Couldn't reach server. Check your internet connection"))
                return@flow
            } else {
                emit(Resource.Success(currenciesFromRoom))
            }
        }
    }.flowOn(Dispatchers.IO)
}