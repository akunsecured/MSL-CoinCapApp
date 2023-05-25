package hu.bme.aut.msl_coincapapp.ui.screen.favorites

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import hu.bme.aut.msl_coincapapp.ui.common.CurrencyItem
import hu.bme.aut.msl_coincapapp.ui.screen.destinations.CurrencyScreenDestination

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun FavoritesScreen(
    navigator: DestinationsNavigator,
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
) {
    val currencies by favoritesViewModel.favoriteCurrencies
    val isRefresh by favoritesViewModel.isRefresh.collectAsState()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefresh)

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
                        "Favorites",
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    ) { padding ->
        SwipeRefresh(
            modifier = Modifier.padding(padding),
            state = swipeRefreshState,
            onRefresh = favoritesViewModel::refreshFavoriteCurrencies
        ) {
            LazyColumn {
                items(currencies) { currency ->
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