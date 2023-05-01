package hu.bme.aut.msl_coincapapp.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.bme.aut.msl_coincapapp.model.Currency

@Database(entities = [Currency::class], version = 1, exportSchema = false)
abstract class CurrencyDatabase() : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}