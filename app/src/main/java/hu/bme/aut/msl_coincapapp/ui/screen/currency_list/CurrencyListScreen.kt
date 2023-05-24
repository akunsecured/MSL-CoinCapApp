package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.msl_coincapapp.model.Currency
import hu.bme.aut.msl_coincapapp.ui.common.CurrencyImage
import hu.bme.aut.msl_coincapapp.ui.screen.destinations.CurrencyScreenDestination
import hu.bme.aut.msl_coincapapp.utils.roundTo

@Destination(start = true)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CurrencyListScreen(
    navigator: DestinationsNavigator,
    currencyListViewModel: CurrencyListViewModel = hiltViewModel(),
) {
    val viewState by currencyListViewModel.viewState
    val isRefresh by currencyListViewModel.isRefresh.collectAsState()

    val swipeRefreshState =
        rememberSwipeRefreshState(isRefreshing = isRefresh && viewState.isLoading)

    val searchWidgetState by currencyListViewModel.searchWidgetState
    val searchTextState by currencyListViewModel.searchTextState

    val context = LocalContext.current

    Scaffold(
        topBar = {
            CurrencyListAppBar(
                searchWidgetState = searchWidgetState,
                searchTextState = searchTextState,
                onTextChange = {
                    currencyListViewModel.updateSearchTextState(it)
                },
                onCloseClicked = {
                    currencyListViewModel.updateSearchTextState("")
                    currencyListViewModel.updateSearchWidgetState(SearchWidgetState.CLOSED)
                },
                onSearchClicked = {
                    Log.d("SearchValue", it)
                },
                onSearchTriggered = {
                    currencyListViewModel.updateSearchWidgetState(SearchWidgetState.OPENED)
                }
            )
        },
    ) { padding ->
        if (viewState.isLoading && !swipeRefreshState.isRefreshing) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            SwipeRefresh(
                modifier = Modifier.padding(padding),
                state = swipeRefreshState,
                onRefresh = currencyListViewModel::refreshCurrencyList
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (viewState.currencies.isEmpty() && viewState.error.isNotEmpty()) {
                        items(1) {
                            Text(
                                text = viewState.error,
                            )
                        }
                    } else {
                        if (viewState.isFromCache && isRefresh && viewState.error.isNotEmpty()) {
                            Toast.makeText(context, viewState.error, Toast.LENGTH_SHORT).show()
                        }
                        items(viewState.currencies) { currency ->
                            CurrencyItem(
                                currency = currency,
                                itemClick = { id ->
                                    navigator.navigate(CurrencyScreenDestination(id))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyListAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            DefaultAppBar(onSearchTriggered)
        }

        SearchWidgetState.OPENED -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = onTextChange,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(onSearchClicked: () -> Unit) {
    TopAppBar(
        modifier = Modifier.background(Color.Blue),
        navigationIcon = {
            IconButton(onClick = {

            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Icon"
                )
            }
        },
        actions = {
            IconButton(onClick = onSearchClicked) {
                Icon(
                    imageVector = Icons.Default.Search,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Search"
                )
            }
        },
        title = {
            Text(
                "Currency list",
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
@Preview
fun DefaultAppBarPreview() {
    DefaultAppBar {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = text,
            onValueChange = onTextChange,
            placeholder = {
                Text(
                    text = "Search here...",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.titleMedium.fontSize,
                color = MaterialTheme.colorScheme.onPrimary,
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()) {
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@Composable
@Preview
fun SearchAppBarPreview() {
    SearchAppBar(
        text = "Some random text",
        onTextChange = {},
        onCloseClicked = {},
        onSearchClicked = {}
    )
}

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