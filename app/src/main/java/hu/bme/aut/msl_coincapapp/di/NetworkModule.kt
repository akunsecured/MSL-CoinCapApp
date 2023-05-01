package hu.bme.aut.msl_coincapapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.msl_coincapapp.network.CoinCapService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideCoinCapService(): CoinCapService {
        return CoinCapService()
    }
}