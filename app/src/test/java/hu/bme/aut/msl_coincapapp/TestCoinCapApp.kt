package hu.bme.aut.msl_coincapapp

import hu.bme.aut.msl_coincapapp.mock.network.MockService
import hu.bme.aut.msl_coincapapp.mock.persistence.MockDao
import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.model.FavoriteStatus
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class TestCoinCapApp {
    private lateinit var dao: MockDao
    private lateinit var coinCapService: MockService

    private val testCurrency = Currency(
        id = "test",
        name = "Test",
        rank = 1,
        symbol = "test"
    )

    private val testStatus = FavoriteStatus(
        id = "test",
    )

    @Before
    fun before() {
        dao = MockDao()
        coinCapService = MockService()
    }

    @After
    fun after() {
        dao.deleteAll()
    }

    @Test
    fun testInsertCurrencyList() = runBlocking {
        dao.insertCurrencyList(
            listOf(
                testCurrency
            )
        )

        val currencies = dao.getAllCurrencies()

        assert(currencies.size == 1)
        assert(currencies[0] == testCurrency)
    }

    @Test
    fun testGetCurrencyById() = runBlocking {
        var currency = dao.getCurrencyById("test")
        assert(currency == null)

        dao.insertCurrencyList(
            listOf(
                testCurrency
            )
        )
        currency = dao.getCurrencyById("test")

        assert(currency != null)
        assert(currency!! == testCurrency)
    }

    @Test
    fun testGetAssets() = runBlocking {
        val result = coinCapService.getAssets()

        assert(result.data.size == 100)
    }

    @Test
    fun testGetSearchedCurrencies() = runBlocking {
        dao.insertCurrencyList(coinCapService.getAssets().data)

        val idSearch = "coin1"
        val nameSearch = "Coin 2"
        val symbolSearch = "C3"

        val results = mutableListOf<Currency>()
        results.addAll(dao.getSearchedCurrencies(idSearch))

        assert(results.size == 12) // 1, 10, ..., 19, 100
        assert(results.filter { currency -> currency.id.startsWith(idSearch) }.toList().size == 12)

        results.clear()
        results.addAll(dao.getSearchedCurrencies(nameSearch))

        assert(results.size == 11)
        assert(results.filter { currency -> currency.name.startsWith(nameSearch) }
            .toList().size == 11)

        results.clear()
        results.addAll(dao.getSearchedCurrencies(symbolSearch))

        assert(results.size == 11)
        assert(results.filter { currency -> currency.symbol.startsWith(symbolSearch) }
            .toList().size == 11)
    }

    @Test
    fun testGetFavoriteStatusById() = runBlocking {
        var status = dao.getFavoriteStatusById("test")
        assert(status == null)

        dao.insertFavoriteStatus(testStatus)
        status = dao.getFavoriteStatusById("test")

        assert(status != null)
        assert(status == testStatus)
    }

    @Test
    fun testUpdateFavoriteStatus() = runBlocking {
        dao.insertFavoriteStatus(testStatus)

        dao.updateFavoriteStatus(testStatus.copy(isFavorite = true))
        val status = dao.getFavoriteStatusById("test")

        assert(status!!.isFavorite)
    }

    @Test
    fun testGetFavoriteCurrencies() = runBlocking {
        val result = coinCapService.getAssets()
        result.data.forEach { currency ->
            dao.insertCurrencyList(listOf(currency))
            dao.insertFavoriteStatus(
                FavoriteStatus(
                    id = currency.id,
                    isFavorite = currency.id == "coin1"
                )
            )
        }

        val favorites = dao.getFavoriteCurrencies()

        assert(favorites.size == 1)
        assert(favorites.first() == result.data.first())
    }
}