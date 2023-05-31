package hu.bme.aut.msl_coincapapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import hu.bme.aut.msl_coincapapp.ui.screen.NavGraphs
import hu.bme.aut.msl_coincapapp.ui.theme.MSLCoinCapAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        analytics = Firebase.analytics
        analytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, null)

        setContent {
            MSLCoinCapAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    navController.addOnDestinationChangedListener { _, destination, bundle ->
                        val params = Bundle()

                        var route = destination.route
                        if (route!!.endsWith("{id}")) {
                            route = route.replace("{id}", bundle!!.getString("id", ""))
                        }

                        Log.d("OnDestinationChangedListener", "Route: $route")

                        params.putString(FirebaseAnalytics.Param.SCREEN_NAME, route)
                        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, params)
                    }

                    DestinationsNavHost(navGraph = NavGraphs.root, navController = navController)
                }
            }
        }
    }
}