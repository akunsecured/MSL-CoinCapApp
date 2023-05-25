package hu.bme.aut.msl_coincapapp.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import hu.bme.aut.msl_coincapapp.model.FavoriteStatus

@Database(entities = [FavoriteStatus::class], version = 1, exportSchema = false)
abstract class FavoriteStatusDatabase : RoomDatabase() {
    abstract fun favoriteStatusDao(): FavoriteStatusDao
}