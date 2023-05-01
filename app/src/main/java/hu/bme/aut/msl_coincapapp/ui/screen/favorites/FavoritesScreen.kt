package hu.bme.aut.msl_coincapapp.ui.screen.favorites

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun FavoritesScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    favoritesViewModel: FavoritesViewModel = viewModel(),
) {
    Text(text = "Favorites")
}