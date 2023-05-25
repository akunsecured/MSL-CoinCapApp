package hu.bme.aut.msl_coincapapp.network

import hu.bme.aut.msl_coincapapp.model.Currency
import kotlinx.serialization.Serializable

@Serializable
data class CoinCapResult(
    val data: List<Currency>,
    val timestamp: Long,
)