package hu.bme.aut.msl_coincapapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currencies")
data class Currency(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "rank") val rank: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "supply") val supply: Double,
)