package hu.bme.aut.msl_coincapapp.network

import retrofit2.http.GET
import retrofit2.http.Query

interface CoinCapService {
    /**
     * Get the cryptocurrencies
     *
     * @param search search by asset id (bitcoin) or symbol (BTC) (optional)
     * @param limit max limit of 2000 (optional)
     * @param offset offset (optional)
     * @return CurrencyResults
     */
    @GET("assets")
    suspend fun getAssets(
        @Query("search") search: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("offset") offset: Int? = null,
    ): CoinCapResult
}