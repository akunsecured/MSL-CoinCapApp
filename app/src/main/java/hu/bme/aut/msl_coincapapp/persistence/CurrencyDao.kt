package hu.bme.aut.msl_coincapapp.persistence

import androidx.room.Dao
import androidx.room.Query
import hu.bme.aut.msl_coincapapp.model.Currency
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Query("SELECT * from currencies ORDER BY name ASC")
    fun getAllCurrencies(): Flow<List<Currency>>
}