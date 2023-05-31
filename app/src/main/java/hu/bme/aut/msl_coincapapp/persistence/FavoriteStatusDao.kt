package hu.bme.aut.msl_coincapapp.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.msl_coincapapp.model.FavoriteStatus

@Dao
interface FavoriteStatusDao {
    @Query("SELECT * from favorite_statuses WHERE id == :id")
    fun getFavoriteStatusById(id: String): FavoriteStatus?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteStatus(favoriteStatus: FavoriteStatus)

    @Update
    suspend fun updateFavoriteStatus(favoriteStatus: FavoriteStatus)
}