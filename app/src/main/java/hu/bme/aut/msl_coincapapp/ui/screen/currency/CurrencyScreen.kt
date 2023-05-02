package hu.bme.aut.msl_coincapapp.ui.screen.currency

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CurrencyScreen(
    modifier: Modifier = Modifier,
    currencyViewModel: CurrencyViewModel = viewModel(),
) {
    Text(text = "Currency")
}