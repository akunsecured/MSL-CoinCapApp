package hu.bme.aut.msl_coincapapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "favorite_statuses")
data class FavoriteStatus(
    @PrimaryKey val id: String = "",
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean = false,
)
