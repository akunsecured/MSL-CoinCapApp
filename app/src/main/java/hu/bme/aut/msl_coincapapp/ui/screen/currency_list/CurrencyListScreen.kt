package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.bme.aut.msl_coincapapp.model.Currency

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyListScreen(
    navController: NavController,
    currencyListViewModel: CurrencyListViewModel,
) {
    val currencyList by currencyListViewModel.currencyList.collectAsState(initial = emptyList())
    val isLoading by currencyListViewModel.isLoading

    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.background(Color.Blue),
                title = { Text("Currency list") })
        },
    ) { padding ->
        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(currencyList) { currency ->
                    CurrencyItem(
                        currency = currency,
                        itemClick = { id ->
                            navController.navigate("currency/$id")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CurrencyItem(
    currency: Currency,
    itemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clickable {
                itemClick(currency.id)
            }
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(all = 8.dp)
        ) {
            Text(text = "${currency.name} (${currency.symbol})")
            Text(text = "Rank #${currency.rank}")
            Text(text = "Price: $${currency.priceUsd}")
        }
    }
}