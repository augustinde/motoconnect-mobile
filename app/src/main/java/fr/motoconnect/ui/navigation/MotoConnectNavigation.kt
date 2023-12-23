package fr.motoconnect.ui.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import fr.motoconnect.R
import fr.motoconnect.ui.screen.HomeScreen
import fr.motoconnect.ui.screen.JourneysScreen
import fr.motoconnect.ui.screen.MotoScreen
import fr.motoconnect.ui.screen.ProfileScreen
import fr.motoconnect.viewmodel.AuthenticationViewModel
import fr.motoconnect.viewmodel.MapViewModel

enum class MotoConnectNavigationRoutes(@DrawableRes val icon: Int) {
    Homepage(icon = R.drawable.homepage),
    Journeys(icon = R.drawable.map),
    Moto(icon = R.drawable.moto),
    Profile(icon = R.drawable.profile)
}

@Composable
fun MotoConnectNavigation(
    navController: NavHostController = rememberNavController(),
    auth: FirebaseAuth,
    authenticationViewModel: AuthenticationViewModel,
    mapViewModel: MapViewModel
) {

    NavHost(
        navController = navController,
        startDestination = MotoConnectNavigationRoutes.Homepage.name,
    ) {
        composable(MotoConnectNavigationRoutes.Homepage.name) {
            HomeScreen(navController = navController, mapViewModel = mapViewModel)
        }

        composable(MotoConnectNavigationRoutes.Journeys.name) {
            JourneysScreen()
        }

        composable(MotoConnectNavigationRoutes.Moto.name) {
            MotoScreen()
        }

        composable(MotoConnectNavigationRoutes.Profile.name) {
            ProfileScreen(
                auth = auth,
                authenticationViewModel = authenticationViewModel)
        }
    }

}