package hu.bme.aut.msl_coincapapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.bme.aut.msl_coincapapp.ui.screen.currency_list.CurrencyListScreen
import hu.bme.aut.msl_coincapapp.ui.screen.currency.CurrencyScreen
import hu.bme.aut.msl_coincapapp.ui.screen.favorites.FavoritesScreen

@Composable
fun CoinCapAppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = "currency_list"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        composable("currency_list") {
            CurrencyListScreen(navController = navController)
        }

        composable("favorites") {
            FavoritesScreen(navController = navController)
        }

        composable("currency/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.StringType }
            )
        ) {
            val currencyId = it.arguments?.getString("id")
            currencyId?.let {
                CurrencyScreen()
            }
        }
    }
}