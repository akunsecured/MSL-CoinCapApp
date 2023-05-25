package hu.bme.aut.msl_coincapapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

/**
 * @param id unique identifier for asset
 * @param rank rank is in ascending order - this number is directly associated with the marketcap whereas the highest marketcap receives rank 1
 * @param symbol most common symbol used to identify this asset on an exchange
 * @param name proper name for asset
 * @param supply available supply for trading
 * @param maxSupply total quantity of asset issued
 * @param marketCapUsd supply x price
 * @param volumeUsd24Hr quantity of trading volume represented in USD over the last 24 hours
 * @param priceUsd volume-weighted price based on real-time market data, translated to USD
 * @param changePercent24Hr the direction and value change in the last 24 hours
 * @param vwap24Hr Volume Weighted Average Price in the last 24 hours
 * @param explorer the website of the currency
 */
@Serializable
@Entity(tableName = "currencies")
data class Currency(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "rank") val rank: Int = -1,
    @ColumnInfo(name = "symbol") val symbol: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "supply") val supply: Double? = 0.0,
    @ColumnInfo(name = "maxSupply") val maxSupply: Double? = 0.0,
    @ColumnInfo(name = "marketCapUsd") val marketCapUsd: Double? = 0.0,
    @ColumnInfo(name = "volumeUsd24Hr") val volumeUsd24Hr: Double? = 0.0,
    @ColumnInfo(name = "priceUsd") val priceUsd: Double? = 0.0,
    @ColumnInfo(name = "changePercent24Hr") val changePercent24Hr: Double? = 0.0,
    @ColumnInfo(name = "vwap24Hr") val vwap24Hr: Double? = 0.0,
    @ColumnInfo(name = "explorer") val explorer: String? = "",
    @ColumnInfo(name = "timestamp") var timestamp: Long = -1,
)