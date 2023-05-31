package hu.bme.aut.msl_coincapapp.ui.common

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import hu.bme.aut.msl_coincapapp.R
import hu.bme.aut.msl_coincapapp.model.Currency

@Composable
fun CurrencyImage(currency: Currency, size: Int = 48) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(
                "https://assets.coincap.io/assets/icons/${
                    currency.symbol.lowercase()
                }@2x.png"
            )
            .crossfade(true)
            .error(R.drawable.ic_launcher_foreground)
            .build(),
        contentDescription = "Cryptocurrency Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(size.dp)
            .clip(CircleShape)
    )
}