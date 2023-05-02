package hu.bme.aut.msl_coincapapp.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDao
import hu.bme.aut.msl_coincapapp.persistence.CurrencyDatabase
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
}