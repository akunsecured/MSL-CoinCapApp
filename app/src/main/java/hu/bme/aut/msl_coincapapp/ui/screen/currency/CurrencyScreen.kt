package hu.bme.aut.msl_coincapapp.ui.screen.currency

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.msl_coincapapp.ui.common.CurrencyImage
import hu.bme.aut.msl_coincapapp.utils.format
import hu.bme.aut.msl_coincapapp.utils.roundTo
import java.text.SimpleDateFormat

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CurrencyScreen(
    navigator: DestinationsNavigator,
    id: String,
    viewModel: CurrencyViewModel = hiltViewModel()
) {
    viewModel.initCurrencyStateById(id)
    val currency by viewModel.currency
    val favoriteStatus by viewModel.favoriteStatus

    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navigator.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = "Navigate back",
                        )
                    }
                },
                title = {
                    Text(
                        text = currency.name,
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = { viewModel.updateFavoriteStatus() }) {
                        Icon(
                            imageVector = if (favoriteStatus.isFavorite) {
                                Icons.Outlined.Favorite
                            } else {
                                Icons.Outlined.FavoriteBorder
                            },
                            tint = MaterialTheme.colorScheme.onPrimary,
                            contentDescription = if (viewModel.favoriteStatus.value.isFavorite) {
                                "Remove from the Favorites"
                            } else {
                                "Save to the Favorites"
                            },
                        )
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .padding(all = 24.dp)
                        .fillMaxWidth()
                ) {
                    Column {
                        Row(
                            Modifier
                                .padding(all = 16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CurrencyImage(currency = currency, size = 96)
                            Column(
                                modifier = Modifier
                                    .padding(start = 16.dp)
                            ) {
                                Text(
                                    text = "Rank ${currency.rank}",
                                    style = TextStyle.Default.copy(
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                )
                                Text(text = "at ${getDateText(currency.timestamp)}")
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 16.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Price")
                                Text(
                                    text = "$${currency.priceUsd.roundTo(2)}",
                                    style = TextStyle.Default.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "(${currency.changePercent24Hr.roundTo(2)}%)",
                                    style = TextStyle.Default.copy(
                                        color = if ((currency.changePercent24Hr ?: 0.0) > 0) {
                                            Color.Green
                                        } else if ((currency.changePercent24Hr ?: 0.0) < 0) {
                                            Color.Red
                                        } else {
                                            TextStyle.Default.color
                                        },
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Volume (24Hr)")
                                Text(
                                    text = "$${currency.volumeUsd24Hr.format()}",
                                    style = TextStyle.Default.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(all = 16.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Supply")
                                Text(
                                    text = "${currency.supply.format()} ${currency.symbol}",
                                    style = TextStyle.Default.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = "Market Cap")
                                Text(
                                    text = "$${currency.marketCapUsd.format()}",
                                    style = TextStyle.Default.copy(
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
                if (currency.explorer != null) {
                    Button(
                        onClick = {
                            uriHandler.openUri(currency.explorer!!)
                        },
                    ) {
                        Text(text = "Go to website")
                    }
                }
            }
        }
    )
}

@SuppressLint("SimpleDateFormat")
private fun getDateText(date: Long): String =
    SimpleDateFormat("yyyy. MM. dd. HH:mm").format(date)