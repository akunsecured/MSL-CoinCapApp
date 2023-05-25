package hu.bme.aut.msl_coincapapp.mock.network

import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.network.CoinCapService
import hu.bme.aut.msl_coincapapp.network.CoinCapResult

class MockService : CoinCapService {
    override suspend fun getAssets(
        search: String?,
        limit: Int?,
        offset: Int?
    ): CoinCapResult {
        val currencies = MutableList(100) {
            val num = it + 1
            Currency(
                id = "coin$num",
                name = "Coin $num",
                symbol = "C$num",
                rank = num,
            )
        }
        return CoinCapResult(
            currencies,
            -1
        )
    }
}