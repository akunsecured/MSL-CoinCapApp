package hu.bme.aut.msl_coincapapp.mock.persistence

import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.model.FavoriteStatus
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDao
import hu.bme.aut.msl_coincapapp.persistence.FavoriteStatusDao

class MockDao : CurrencyDao, FavoriteStatusDao {
    private val currencies = mutableListOf<Currency>()
    private val favoriteStatuses = mutableListOf<FavoriteStatus>()

    fun deleteAll() {
        currencies.clear()
        favoriteStatuses.clear()
    }

    override fun getAllCurrencies(): List<Currency> = currencies

    override fun getSearchedCurrencies(search: String): List<Currency> =
        currencies.filter { currency ->
            currency.id.startsWith(search) || currency.name.startsWith(
                search
            ) || currency.symbol.startsWith(search)
        }.toList()

    override fun getFavoriteCurrencies(): List<Currency> =
        currencies.filter { currency ->
            (favoriteStatuses.filter { status -> status.isFavorite }.toList()
                .map { status -> status.id }).contains(currency.id)
        }

    override fun getCurrencyById(id: String): Currency? =
        currencies.find { currency -> currency.id == id }

    override suspend fun insertCurrencyList(currencies: List<Currency>) {
        this.currencies.addAll(currencies)
    }

    override fun getFavoriteStatusById(id: String): FavoriteStatus? =
        favoriteStatuses.find { status -> status.id == id }

    override suspend fun insertFavoriteStatus(favoriteStatus: FavoriteStatus) {
        favoriteStatuses.add(favoriteStatus)
    }

    override suspend fun updateFavoriteStatus(favoriteStatus: FavoriteStatus) {
        favoriteStatuses[favoriteStatuses.indexOfFirst { status -> status.id == favoriteStatus.id }] =
            favoriteStatus
    }
}