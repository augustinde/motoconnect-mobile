package fr.motoconnect

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import fr.motoconnect.ui.navigation.AuthenticationNavigation
import fr.motoconnect.ui.navigation.MotoConnectNavigation
import fr.motoconnect.ui.navigation.MotoConnectNavigationRoutes
import fr.motoconnect.ui.store.DisplayStore
import fr.motoconnect.ui.theme.MotoConnectTheme
import fr.motoconnect.viewmodel.AuthenticationViewModel
import fr.motoconnect.viewmodel.MapViewModel

class MainActivity : ComponentActivity() {

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) -> {
                    Log.i(TAG, "Location permission ACCESS_FINE_LOCATION granted")
                }

                permissions.getOrDefault(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                ) -> {
                    Log.i(TAG, "Location permission ACCESS_COARSE_LOCATION granted")
                }

                else -> {
                    Log.i(TAG, "Some permissions denied")
                }
            }
        }

        locationPermissionRequest.launch(
            arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )

        val auth = FirebaseAuth.getInstance()
        val db = Firebase.firestore

        val authenticationViewModel = AuthenticationViewModel(auth, db, applicationContext)

        setContent {
            MotoConnectTheme(activated = getDisplayStore(applicationContext)) {
                MainScreen(
                    auth = auth,
                    authenticationViewModel = authenticationViewModel
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    auth: FirebaseAuth,
    authenticationViewModel: AuthenticationViewModel
) {
    val navController = rememberNavController()
    val mapViewModel: MapViewModel = viewModel(factory = MapViewModel.Factory)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val authUIState by authenticationViewModel.authUiState.collectAsState()

    Log.d("TAG", "MainScreen: " + auth.currentUser?.email + " " + authUIState.isLogged)

    if (auth.currentUser != null && authUIState.isLogged) {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    backgroundColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.tertiary
                ) {
                    MotoConnectNavigationRoutes.values().forEach { item ->
                        if (item.displayInBar) {
                            BottomNavigationItem(
                                selected = currentDestination?.hierarchy?.any { it.route == item.name } == true,
                                selectedContentColor = MaterialTheme.colorScheme.secondary,
                                unselectedContentColor = MaterialTheme.colorScheme.tertiary,
                                onClick = {
                                    navController.navigate(item.name) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                icon = {
                                    Icon(
                                        painter = painterResource(id = item.icon),
                                        contentDescription = "Icon ${item.name}",
                                        modifier = Modifier
                                            .size(30.dp)
                                            .padding(0.dp, 0.dp, 0.dp, 0.dp),
                                    )
                                }
                            )
                        }

                    }
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                MotoConnectNavigation(
                    navController = navController,
                    auth = auth,
                    authenticationViewModel = authenticationViewModel,
                    mapViewModel = mapViewModel
                )
            }
        }
    } else {
        AuthenticationNavigation(authenticationViewModel = authenticationViewModel)
    }
}

@Composable
fun getDisplayStore(context: Context): Boolean {

    val store = DisplayStore(context)
    val darkmode = store.getDarkMode.collectAsState(initial = false)
    return darkmode.value

}