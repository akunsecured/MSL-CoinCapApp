package hu.bme.aut.msl_coincapapp.ui.screen.currency_list

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
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
import hu.bme.aut.msl_coincapapp.model.MenuItem
import hu.bme.aut.msl_coincapapp.ui.common.CurrencyItem
import hu.bme.aut.msl_coincapapp.ui.screen.destinations.CurrencyScreenDestination
import hu.bme.aut.msl_coincapapp.ui.screen.destinations.FavoritesScreenDestination
import kotlinx.coroutines.launch

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

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val drawerItems = listOf(
        MenuItem(
            id = "favorites",
            title = "Favorites",
            contentDescription = "Go to Favorites Screen",
            icon = Icons.Default.Favorite,
        )
    )

    val searchWidgetState by currencyListViewModel.searchWidgetState
    val searchTextState by currencyListViewModel.searchTextState

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 64.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "Header", fontSize = 60.sp)
                }

                drawerItems.forEach { item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title)
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.contentDescription
                            )
                        },
                        selected = false,
                        onClick = {
                            when (item.id) {
                                "favorites" -> {
                                    navigator.navigate(FavoritesScreenDestination())
                                    scope.launch {
                                        drawerState.close()
                                    }
                                }

                                else -> {
                                    Log.d("DrawerListItem", item.id)
                                }
                            }
                        }
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CurrencyListAppBar(
                    searchWidgetState = searchWidgetState,
                    searchTextState = searchTextState,
                    onTextChange = {
                        currencyListViewModel.updateSearchTextState(it)
                    },
                    onCloseClicked = {
                        currencyListViewModel.apply {
                            updateSearchTextState("")
                            updateSearchWidgetState(SearchWidgetState.CLOSED)
                            getCurrencyList()
                        }
                    },
                    onSearchClicked = {
                        currencyListViewModel.searchCurrencies(it.lowercase())
                    },
                    onSearchTriggered = {
                        currencyListViewModel.updateSearchWidgetState(SearchWidgetState.OPENED)
                    },
                    onDrawerIconClicked = {
                        scope.launch {
                            drawerState.open()
                        }
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
                    val errorCheck = viewState.currencies.isEmpty() && viewState.error.isNotEmpty()

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = if (errorCheck) Arrangement.Center else Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (errorCheck) {
                            items(1) {
                                Text(
                                    text = viewState.error,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            if (viewState.isFromCache && viewState.error.isNotEmpty() && isRefresh) {
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
}

@Composable
fun CurrencyListAppBar(
    searchWidgetState: SearchWidgetState,
    searchTextState: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    onDrawerIconClicked: () -> Unit,
) {
    when (searchWidgetState) {
        SearchWidgetState.CLOSED -> {
            DefaultAppBar(
                onSearchTriggered,
                onDrawerIconClicked
            )
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
fun DefaultAppBar(
    onSearchClicked: () -> Unit,
    onDrawerIconClicked: () -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.background(Color.Blue),
        navigationIcon = {
            IconButton(onClick = onDrawerIconClicked) {
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
    DefaultAppBar({}) {}
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