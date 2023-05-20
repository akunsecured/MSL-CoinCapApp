package hu.bme.aut.msl_coincapapp.ui.screen.currency

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.msl_coincapapp.model.Currency

@Destination
@Composable
fun CurrencyScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    currency: Currency,
) {
    Text(text = currency.name)
}