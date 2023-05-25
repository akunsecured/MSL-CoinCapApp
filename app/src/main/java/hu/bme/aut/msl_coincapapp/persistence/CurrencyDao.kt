package hu.bme.aut.msl_coincapapp.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hu.bme.aut.msl_coincapapp.model.Currency

@Dao
interface CurrencyDao {
    @Query("SELECT * from currencies ORDER BY rank ASC")
    fun getAllCurrencies(): List<Currency>

    @Query("SELECT * from currencies ORDER BY rank ASC LIMIT :limit OFFSET :offset")
    fun getNextCurrencies(offset: Int, limit: Int): List<Currency>

    @Query("SELECT * from currencies WHERE (LOWER(symbol) LIKE '%' || :search || '%' OR id LIKE '%' || :search || '%' OR LOWER(name) LIKE '%' || :search || '%') ORDER BY rank ASC")
    fun getSearchedCurrencies(search: String): List<Currency>

    @Query("SELECT currencies.* from currencies INNER JOIN favorite_statuses ON favorite_statuses.id = currencies.id WHERE isFavorite = 1")
    fun getFavoriteCurrencies(): List<Currency>

    @Query("SELECT * from currencies WHERE id = :id")
    fun getCurrencyById(id: String): Currency?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCurrencyList(currencies: List<Currency>)
}