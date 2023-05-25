package hu.bme.aut.msl_coincapapp.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.model.FavoriteStatus

@Database(entities = [Currency::class, FavoriteStatus::class], version = 3, exportSchema = false)
abstract class AppDatabase() : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
    abstract fun favoriteStatusDao(): FavoriteStatusDao
}