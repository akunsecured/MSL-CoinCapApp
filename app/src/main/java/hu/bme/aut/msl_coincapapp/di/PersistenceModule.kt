package hu.bme.aut.msl_coincapapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDao
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDatabase
import hu.bme.aut.msl_coincapapp.persistence.FavoriteStatusDao
import hu.bme.aut.msl_coincapapp.persistence.FavoriteStatusDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    @Provides
    @Singleton
    fun provideCurrencyDatabase(application: Application): CurrencyDatabase {
        return Room.databaseBuilder(application, CurrencyDatabase::class.java, "currency_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideCurrencyDao(database: CurrencyDatabase): CurrencyDao {
        return database.currencyDao()
    }

    @Provides
    @Singleton
    fun provideFavoriteStatusDatabase(application: Application): FavoriteStatusDatabase {
        return Room.databaseBuilder(
            application,
            FavoriteStatusDatabase::class.java,
            "favorite_status_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteStatusDao(database: FavoriteStatusDatabase): FavoriteStatusDao {
        return database.favoriteStatusDao()
    }
}