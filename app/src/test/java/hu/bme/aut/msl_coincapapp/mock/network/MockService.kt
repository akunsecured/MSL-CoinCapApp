package hu.bme.aut.msl_coincapapp.mock.network

import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.network.CoinCapService
import hu.bme.aut.msl_coincapapp.network.CurrencyResults

class MockService : CoinCapService {
    override suspend fun getAssets(
        search: String?,
        limit: Int?,
        offset: Int?
    ): CurrencyResults {
        val currencies = MutableList(100) {
            val num = it + 1
            Currency(
                id = "coin$num",
                name = "Coin $num",
                symbol = "C$num",
                rank = num,
            )
        }
        return CurrencyResults(
            currencies,
            -1
        )
    }
}