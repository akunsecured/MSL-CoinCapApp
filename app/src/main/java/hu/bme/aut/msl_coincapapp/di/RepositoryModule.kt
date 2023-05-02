package hu.bme.aut.msl_coincapapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import hu.bme.aut.msl_coincapapp.network.CoinCapService
import hu.bme.aut.msl_coincapapp.ui.screen.currency.CurrencyRepository
import hu.bme.aut.msl_coincapapp.ui.screen.currency_list.CurrencyListRepository
import hu.bme.aut.msl_coincapapp.ui.screen.favorites.FavoritesRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideCurrencyListRepository(
        coinCapService: CoinCapService
    ): CurrencyListRepository = CurrencyListRepository(coinCapService)

    @Provides
    @ViewModelScoped
    fun provideCurrencyRepository(): CurrencyRepository = CurrencyRepository()

    @Provides
    @ViewModelScoped
    fun provideFavoritesRepository(): FavoritesRepository = FavoritesRepository()
}