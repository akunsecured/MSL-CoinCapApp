package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun CurrencyListScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    currencyListViewModel: CurrencyListViewModel = viewModel(),
) {
    Text(text = "Currency List")
}