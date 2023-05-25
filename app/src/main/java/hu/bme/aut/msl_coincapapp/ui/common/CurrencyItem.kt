package hu.bme.aut.msl_coincapapp.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.utils.roundTo

@Composable
fun CurrencyItem(
    currency: Currency,
    itemClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .padding(all = 8.dp)
            .clickable {
                itemClick(currency.id)
            }
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary.copy(
                alpha = 0.2f
            )
        )
    ) {
        Row(
            Modifier
                .padding(all = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row {
                CurrencyImage(currency = currency)
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                ) {
                    Text(
                        text = currency.name,
                        style = TextStyle.Default.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    Text(text = "(${currency.symbol})")
                }
            }
            Row {
                Text(
                    text = "$${currency.priceUsd?.roundTo(2)}",
                    style = TextStyle.Default.copy(
                        textAlign = TextAlign.End,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "${currency.changePercent24Hr?.roundTo(2)}%",
                    style = TextStyle.Default.copy(
                        textAlign = TextAlign.End,
                        color = if (currency.changePercent24Hr!! >= 0) {
                            Color.Green
                        } else {
                            Color.Red
                        },
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .widthIn(88.dp, 88.dp)
                        .padding(horizontal = 8.dp)
                )
            }
        }

    }
}

@Composable
@Preview
fun CurrencyItemPreview() {
    CurrencyItem(
        currency = Currency(
            id = "example",
            rank = 1,
            name = "Long-Example-Coin",
            symbol = "EC",
            priceUsd = 12345.6789,
            changePercent24Hr = -1234.5678,
        ),
        itemClick = {})
}