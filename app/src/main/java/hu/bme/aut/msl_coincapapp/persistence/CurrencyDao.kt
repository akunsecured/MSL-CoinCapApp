package hu.bme.aut.msl_coincapapp.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import hu.bme.aut.msl_coincapapp.model.Currency

@Dao
interface CurrencyDao {
    @Query("SELECT * from currencies ORDER BY rank ASC")
    fun getAllCurrencies(): List<Currency>

    @Query("SELECT currencies.* from currencies INNER JOIN favorite_statuses ON favorite_statuses.id = currencies.id WHERE isFavorite = 1")
    fun getFavoriteCurrencies(): List<Currency>

    @Query("SELECT * from currencies WHERE id = :id")
    fun getCurrencyById(id: String): Currency?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyList(currencies: List<Currency>)

    @Update
    suspend fun update(currency: Currency)

    @Delete
    suspend fun delete(currency: Currency)
}